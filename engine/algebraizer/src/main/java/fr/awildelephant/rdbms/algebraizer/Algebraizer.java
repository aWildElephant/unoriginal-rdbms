package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.algebraizer.formula.SubqueryExtractor;
import fr.awildelephant.rdbms.algebraizer.formula.SubqueryJoiner;
import fr.awildelephant.rdbms.ast.*;
import fr.awildelephant.rdbms.ast.value.ScalarSubquery;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.plan.*;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.alias.ColumnAlias;
import fr.awildelephant.rdbms.plan.alias.ColumnAliasBuilder;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.plan.sort.SortSpecification;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.awildelephant.rdbms.algebraizer.ASTToValueExpressionTransformer.createValueExpression;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.innerJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.JoinOutputSchemaFactory.leftJoinOutputSchema;
import static fr.awildelephant.rdbms.plan.alias.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.plan.join.JoinType.SEMI;
import static fr.awildelephant.rdbms.plan.sort.SortSpecification.ascending;
import static fr.awildelephant.rdbms.plan.sort.SortSpecification.descending;
import static fr.awildelephant.rdbms.schema.Schema.EMPTY_SCHEMA;
import static java.util.stream.Collectors.toList;

public final class Algebraizer extends DefaultASTVisitor<LogicalOperator> {

    private final Storage storage;
    private final Schema outerQuerySchema;

    private final AliasExtractor aliasExtractor;
    private final ExpressionSplitter expressionSplitter;
    private final ColumnNameResolver columnNameResolver;
    private final ColumnReferenceTransformer columnReferenceTransformer;

    public Algebraizer(Storage storage, ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer, ExpressionSplitter expressionSplitter, AliasExtractor aliasExtractor) {
        this(storage, EMPTY_SCHEMA, columnNameResolver, columnReferenceTransformer, expressionSplitter, aliasExtractor);
    }

    public Algebraizer(Storage storage, Schema outerQuerySchema, ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer, ExpressionSplitter expressionSplitter, AliasExtractor aliasExtractor) {
        this.storage = storage;
        this.outerQuerySchema = outerQuerySchema;
        this.columnNameResolver = columnNameResolver;
        this.columnReferenceTransformer = columnReferenceTransformer;
        this.expressionSplitter = expressionSplitter;
        this.aliasExtractor = aliasExtractor;

    }

    private Algebraizer withOuterQuerySchema(Schema outerQuerySchema) {
        return new Algebraizer(storage, outerQuerySchema, columnNameResolver, columnReferenceTransformer, expressionSplitter, aliasExtractor);
    }

    @Override
    public LogicalOperator visit(Distinct distinct) {
        return new DistinctLop(apply(distinct.input()));
    }

    @Override
    public LogicalOperator visit(InnerJoin innerJoin) {
        final LogicalOperator leftInput = apply(innerJoin.left());
        final LogicalOperator rightInput = apply(innerJoin.right());

        final Schema outputSchema = innerJoinOutputSchema(leftInput.schema(), rightInput.schema());

        return new InnerJoinLop(leftInput,
                rightInput,
                createValueExpression(innerJoin.joinSpecification(), outputSchema, outerQuerySchema),
                outputSchema);
    }

    @Override
    public LogicalOperator visit(LeftJoin leftJoin) {
        final LogicalOperator leftInput = apply(leftJoin.left());
        final LogicalOperator rightInput = apply(leftJoin.right());

        final Schema outputSchema = leftJoinOutputSchema(leftInput.schema(), rightInput.schema());

        return new LeftJoinLop(leftInput,
                rightInput,
                createValueExpression(leftJoin.joinSpecification(), outputSchema, outerQuerySchema),
                outputSchema);
    }

    @Override
    public LogicalOperator visit(Limit limit) {
        return new LimitLop(apply(limit.input()), limit.limit());
    }

    @Override
    public LogicalOperator visit(ScalarSubquery scalarSubquery) {
        return new ScalarSubqueryLop(apply(scalarSubquery.input()));
    }

    @Override
    public LogicalOperator visit(Select select) {
        LogicalOperator plan = apply(select.fromClause());

        final AST whereClause = select.whereClause();
        if (whereClause != null) {
            final SplitExpressionCollector filter = new SplitExpressionCollector();
            expressionSplitter.split(whereClause, filter);

            if (!filter.aggregates().isEmpty()) {
                throw new IllegalArgumentException("Aggregate are not allowed in the where clause");
            }

            final List<SubqueryJoiner> joiners = filter.subqueries();

            final Schema filterInputSchema = plan.schema();

            if (!joiners.isEmpty()) {
                final Algebraizer outerQueryAwareAlgebraizer = withOuterQuerySchema(filterInputSchema);

                for (SubqueryJoiner joiner : joiners) {
                    final LogicalOperator rightInput = outerQueryAwareAlgebraizer.apply(joiner.subquery());

                    if (joiner.type() == SEMI) {
                        final UnqualifiedColumnReference outputColumn
                                = new UnqualifiedColumnReference(joiner.identifier());
                        if (joiner.predicate() != null) {
                            final ValueExpression semiJoinPredicate
                                    = createValueExpression(joiner.predicate(),
                                    innerJoinOutputSchema(plan.schema(), rightInput.schema()),
                                    outerQuerySchema);
                            plan = new DependentSemiJoinLop(plan,
                                    rightInput,
                                    semiJoinPredicate,
                                    outputColumn);
                        } else {
                            plan = new DependentSemiJoinLop(plan, rightInput, outputColumn);
                        }
                    } else {
                        plan = new DependentJoinLop(plan, rightInput);
                    }
                }
            }

            plan = createFilter(plan, filter.mapsAboveAggregates().get(0));

            if (!joiners.isEmpty()) {
                plan = new ProjectionLop(plan, filterInputSchema.columnNames());
            }
        }

        final SplitExpressionCollector havingAndOutputColumns = new SplitExpressionCollector();
        final AliasCollector aliasCollector = new AliasCollector();

        for (AST column : expandAsterisks(select.outputColumns(), plan.schema())) {
            aliasExtractor.extractAlias(column, aliasCollector);
        }

        final List<AST> outputColumns = aliasCollector.unaliasedColumns();

        for (AST column : outputColumns) {
            expressionSplitter.split(column, havingAndOutputColumns);
        }

        final AST havingClause = select.havingClause();
        final AST havingFilter;
        final SubqueryExtractor havingSubqueryExtractor = new SubqueryExtractor();
        if (havingClause != null) {
            havingFilter = havingSubqueryExtractor.apply(havingClause);

            expressionSplitter.split(havingFilter, havingAndOutputColumns);
        } else {
            havingFilter = null;
        }

        final List<AST> mapsBelowAggregates = havingAndOutputColumns.mapsBelowAggregates();
        if (!mapsBelowAggregates.isEmpty()) {
            plan = createMap(plan, mapsBelowAggregates);
        }

        final List<Aggregate> aggregates = havingAndOutputColumns.aggregates();
        if (!aggregates.isEmpty()) {
            final Schema aggregationInputSchema = plan.schema();
            final List<ColumnReference> breakdowns = Optional.ofNullable(select.groupByClause())
                    .map(groupingSetsList -> groupingSetsList.columns().stream()
                            .map(columnReferenceTransformer)
                            .map(aggregationInputSchema::normalize)
                            .collect(toList()))
                    .orElseGet(List::of);

            plan = new AggregationLop(plan, breakdowns, aggregates);
        }

        plan = mergeInputWithHavingClauseSubqueries(plan, havingSubqueryExtractor.subqueries());

        final List<AST> mapsAboveAggregates = havingAndOutputColumns.mapsAboveAggregates();
        if (!mapsAboveAggregates.isEmpty()) {
            plan = createMap(plan, mapsAboveAggregates);
        }

        if (havingFilter != null) {
            plan = createFilter(plan, unqualifiedColumnName(columnNameResolver.apply(havingFilter)));
        }

        plan = createProjection(plan, outputColumns);

        final Optional<ColumnAlias> aliasing = aliasCollector.aliasing();
        if (aliasing.isPresent()) {
            plan = new AliasLop(plan, aliasing.get());
        }

        final SortSpecificationList orderByClause = select.orderByClause();
        if (orderByClause != null) {
            final List<SortSpecification> specifications = orderByClause.sortSpecificationList()
                    .stream()
                    .map(element -> {
                        final ColumnReference reference = columnReferenceTransformer.apply(element.sortKey());
                        if (element.ordering() == OrderingSpecification.DESCENDING) {
                            return descending(reference);
                        } else {
                            return ascending(reference);
                        }
                    })
                    .collect(toList());
            plan = new SortLop(plan, specifications);
        }

        return plan;
    }

    private LogicalOperator createProjection(LogicalOperator input, List<AST> columnExpressions) {
        final List<ColumnReference> columnReferences = new ArrayList<>(columnExpressions.size());
        final Schema inputSchema = input.schema();

        for (AST columnExpression : columnExpressions) {
            final ColumnReference columnReference = columnReferenceTransformer.apply(columnExpression);
            columnReferences.add(inputSchema.column(columnReference).name());
        }

        return new ProjectionLop(input, columnReferences);
    }

    private LogicalOperator createMap(LogicalOperator input, List<AST> expressions) {
        final List<AST> maps = expressions.stream()
                .filter(expression -> !(expression instanceof ColumnName))
                .collect(toList());

        if (maps.isEmpty()) {
            return input;
        }

        final List<ValueExpression> valueExpressions = createValueExpressions(input, maps);

        final List<ColumnReference> outputNames = mapOutputNames(maps);

        return new MapLop(input, valueExpressions, outputNames);
    }

    private List<ColumnReference> mapOutputNames(List<AST> expressions) {
        final List<ColumnReference> outputNames = new ArrayList<>(expressions.size());

        for (AST expression : expressions) {
            outputNames.add(new UnqualifiedColumnReference(columnNameResolver.apply(expression)));
        }

        return outputNames;
    }

    private List<ValueExpression> createValueExpressions(LogicalOperator input, List<AST> expressions) {
        final List<ValueExpression> valueExpressions = new ArrayList<>(expressions.size());

        for (AST expression : expressions) {
            valueExpressions.add(createValueExpression(expression, input.schema(), outerQuerySchema));
        }

        return valueExpressions;
    }

    private List<AST> expandAsterisks(List<? extends AST> columns, Schema inputSchema) {
        final AsteriskExpander expander = new AsteriskExpander(inputSchema);

        return columns.stream().flatMap(expander).collect(toList());
    }

    private LogicalOperator createFilter(LogicalOperator input, AST filter) {
        return new FilterLop(input, createValueExpression(filter, input.schema(), outerQuerySchema));
    }

    @Override
    public LogicalOperator visit(TableAlias tableAlias) {
        final LogicalOperator input = apply(tableAlias.input());

        final Optional<String> source = input.schema().columnNames().stream().map(ColumnReference::table).filter(
                        Optional::isPresent)
                .map(Optional::get).findAny();

        return new AliasLop(input, tableAlias(source.orElse(null), tableAlias.alias()));
    }

    @Override
    public LogicalOperator visit(TableAliasWithColumns tableAliasWithColumns) {
        final LogicalOperator input = apply(tableAliasWithColumns.input());

        final ColumnAliasBuilder columnAliasBuilder = new ColumnAliasBuilder();
        final List<ColumnReference> columnReferences = input.schema().columnNames();
        for (int i = 0; i < columnReferences.size(); i++) {
            columnAliasBuilder.add(columnReferences.get(i), tableAliasWithColumns.columnAliases().get(i));
        }

        final Optional<String> sourceTable = input.schema().columnNames().stream().map(ColumnReference::table).filter(
                        Optional::isPresent)
                .map(Optional::get).findAny();

        return new AliasLop(new AliasLop(input,
                tableAlias(sourceTable.orElse(null), tableAliasWithColumns.tableAlias())),
                columnAliasBuilder.build().orElseThrow());
    }

    @Override
    public LogicalOperator visit(TableName tableReference) {
        return storage.getOperator(tableReference.name());
    }

    @Override
    public LogicalOperator visit(TableReferenceList tableReferenceList) {
        final LogicalOperator firstInput = apply(tableReferenceList.first());
        final LogicalOperator secondInput = apply(tableReferenceList.second());

        Schema outputSchema = innerJoinOutputSchema(firstInput.schema(), secondInput.schema());

        CartesianProductLop cartesianProduct = new CartesianProductLop(firstInput, secondInput, outputSchema);

        for (AST other : tableReferenceList.others()) {
            cartesianProduct = cartesianProduct(cartesianProduct, other);
        }

        return cartesianProduct;
    }

    private CartesianProductLop cartesianProduct(LogicalOperator left, AST right) {
        final LogicalOperator otherInput = apply(right);
        final Schema outputSchema = innerJoinOutputSchema(left.schema(), otherInput.schema());

        return new CartesianProductLop(left, otherInput, outputSchema);
    }

    @Override
    public LogicalOperator visit(Values values) {
        final List<List<ValueExpression>> matrix = new ArrayList<>();

        for (Row row : values.rows()) {
            final List<AST> expressions = row.values();
            final List<ValueExpression> valueExpressions = new ArrayList<>();

            for (AST expression : expressions) {
                valueExpressions.add(createValueExpression(expression, EMPTY_SCHEMA, outerQuerySchema));
            }

            matrix.add(valueExpressions);
        }

        return new TableConstructorLop(matrix);
    }

    private LogicalOperator mergeInputWithHavingClauseSubqueries(LogicalOperator input, List<SubqueryJoiner> joiners) {
        if (joiners.isEmpty()) {
            return input;
        } else {
            LogicalOperator mergedInput = input;

            for (SubqueryJoiner joiner : joiners) {
                if (joiner.type() == SEMI) {
                    throw new UnsupportedOperationException();
                }

                mergedInput = cartesianProduct(mergedInput, joiner.subquery());
            }

            return mergedInput;
        }
    }

    @Override
    public LogicalOperator defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

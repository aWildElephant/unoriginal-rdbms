package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.ast.InnerJoin;
import fr.awildelephant.rdbms.ast.Limit;
import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.ast.TableAlias;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.TableReferenceList;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.value.ScalarSubquery;
import fr.awildelephant.rdbms.engine.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.AggregationLop;
import fr.awildelephant.rdbms.plan.AliasLop;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.CollectLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LimitLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.MapLop;
import fr.awildelephant.rdbms.plan.ProjectionLop;
import fr.awildelephant.rdbms.plan.ScalarSubqueryLop;
import fr.awildelephant.rdbms.plan.SortLop;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.plan.aggregation.Aggregate;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static fr.awildelephant.rdbms.algebraizer.ASTToValueExpressionTransformer.createValueExpression;
import static fr.awildelephant.rdbms.algebraizer.SchemaValidator.schemaValidator;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.plan.alias.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.plan.alias.TableAlias.tableAlias;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class Algebraizer extends DefaultASTVisitor<LogicalOperator> {

    private static final Schema EMPTY_SCHEMA = new Schema(emptyList());

    private final Storage storage;

    private final AliasExtractor aliasExtractor;
    private final ExpressionSplitter expressionSplitter;
    private final ColumnNameResolver columnNameResolver;
    private final ColumnReferenceTransformer columnReferenceTransformer;

    public Algebraizer(Storage storage) {
        this.storage = storage;

        columnNameResolver = new ColumnNameResolver();
        columnReferenceTransformer = new ColumnReferenceTransformer(columnNameResolver);
        expressionSplitter = new ExpressionSplitter(columnNameResolver, columnReferenceTransformer);
        aliasExtractor = new AliasExtractor(columnReferenceTransformer);
    }

    @Override
    public LogicalOperator visit(Distinct distinct) {
        return new DistinctLop(apply(distinct.input()));
    }

    @Override
    public LogicalOperator visit(InnerJoin innerJoin) {
        final LogicalOperator leftInput = apply(innerJoin.left());
        final LogicalOperator rightInput = apply(innerJoin.right());

        final Schema outputSchema = joinOutputSchema(leftInput.schema(), rightInput.schema());

        return new InnerJoinLop(leftInput,
                                rightInput,
                                createValueExpression(innerJoin.joinSpecification(), outputSchema),
                                outputSchema);
    }

    @Override
    public LogicalOperator visit(Limit limit) {
        return new LimitLop(apply(limit.input()), limit.limit());
    }

    @Override
    public LogicalOperator visit(ScalarSubquery scalarSubquery) {
        return new ScalarSubqueryLop(apply(scalarSubquery.input()), scalarSubquery.id());
    }

    @Override
    public LogicalOperator visit(Select select) {
        LogicalOperator plan = apply(select.fromClause());

        final Optional<AST> whereClause = select.whereClause();
        if (whereClause.isPresent()) {
            final SubqueryExtractor subqueryExtractor = new SubqueryExtractor();
            final AST whereFilter = subqueryExtractor.apply(whereClause.get());

            plan = mergeInputWithSubqueries(plan, subqueryExtractor.subqueries());

            plan = createFilter(plan, whereFilter);
        }

        final Optional<GroupingSetsList> groupByClause = select.groupByClause();
        if (groupByClause.isPresent()) {
            plan = new BreakdownLop(plan, groupByClause.get().breakdowns());
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

        final Optional<AST> havingClause = select.havingClause();
        final AST havingFilter;
        if (havingClause.isPresent()) {
            final SubqueryExtractor havingSubqueryExtractor = new SubqueryExtractor();
            havingFilter = havingSubqueryExtractor.apply(havingClause.get());

            expressionSplitter.split(havingFilter, havingAndOutputColumns);

            plan = mergeInputWithSubqueries(plan, havingSubqueryExtractor.subqueries());
        } else {
            havingFilter = null;
        }

        final List<AST> mapsBelowAggregates = havingAndOutputColumns.mapsBelowAggregates();
        if (!mapsBelowAggregates.isEmpty()) {
            plan = createMap(plan, mapsBelowAggregates);
        }

        final List<Aggregate> aggregates = havingAndOutputColumns.aggregates();
        if (!aggregates.isEmpty()) {
            plan = new AggregationLop(aggregates, plan);
        }

        final List<AST> mapsAboveAggregates = havingAndOutputColumns.mapsAboveAggregates();
        if (!mapsAboveAggregates.isEmpty()) {
            plan = createMap(plan, mapsAboveAggregates);
        }

        if (havingFilter != null) {
            plan = createFilter(plan, unqualifiedColumnName(columnNameResolver.apply(havingFilter)));
        }

        plan = createProjection(plan, outputColumns);

        final Map<String, Map<String, String>> aliasing = aliasCollector.aliasing();
        if (!aliasing.isEmpty()) {
            plan = new AliasLop(plan, columnAlias(aliasing));
        }

        if (groupByClause.isPresent()) {
            plan = new CollectLop(plan);
        }

        final Optional<SortSpecificationList> orderByClause = select.orderByClause();
        if (orderByClause.isPresent()) {
            plan = new SortLop(plan, orderByClause.get().columns());
        }

        return plan;
    }

    private LogicalOperator createProjection(LogicalOperator input, List<AST> columns) {
        final List<ColumnReference> columnReferences = new ArrayList<>(columns.size());

        for (AST column : columns) {
            columnReferences.add(columnReferenceTransformer.apply(column));
        }

        return new ProjectionLop(input, columnReferences);
    }

    private LogicalOperator createMap(LogicalOperator input, List<AST> maps) {
        final List<ValueExpression> valueExpressions = createValueExpressions(input, maps);

        final List<String> outputNames = getOutputNames(maps);

        return new MapLop(input, valueExpressions, outputNames);
    }

    private List<String> getOutputNames(List<AST> expressions) {
        final List<String> outputNames = new ArrayList<>(expressions.size());

        for (AST expression : expressions) {
            outputNames.add(columnNameResolver.apply(expression));
        }

        return outputNames;
    }

    private List<ValueExpression> createValueExpressions(LogicalOperator input, List<AST> expressions) {
        final List<ValueExpression> valueExpressions = new ArrayList<>(expressions.size());

        for (AST expression : expressions) {
            valueExpressions.add(createValueExpression(expression, input.schema()));
        }

        return valueExpressions;
    }

    private List<AST> expandAsterisks(List<? extends AST> columns, Schema inputSchema) {
        final AsteriskExpander expander = new AsteriskExpander(inputSchema);

        return columns.stream().flatMap(expander).collect(toList());
    }

    private LogicalOperator createFilter(LogicalOperator input, AST filter) {
        return new FilterLop(input, createValueExpression(filter, input.schema()));
    }

    @Override
    public LogicalOperator visit(TableAlias tableAlias) {
        final LogicalOperator input = apply(tableAlias.input());

        return new AliasLop(input, tableAlias(tableAlias.alias()));
    }

    @Override
    public LogicalOperator visit(TableName tableReference) {
        final String name = tableReference.name();
        final Table table = storage.get(name);

        return new BaseTableLop(name, table.schema());
    }

    @Override
    public LogicalOperator visit(TableReferenceList tableReferenceList) {
        final LogicalOperator firstInput = apply(tableReferenceList.first());
        final LogicalOperator secondInput = apply(tableReferenceList.second());

        Schema outputSchema = joinOutputSchema(firstInput.schema(), secondInput.schema());

        CartesianProductLop cartesianProduct = new CartesianProductLop(firstInput, secondInput, outputSchema);

        for (AST other : tableReferenceList.others()) {
            cartesianProduct = cartesianProduct(cartesianProduct, other);
        }

        return cartesianProduct;
    }

    private CartesianProductLop cartesianProduct(LogicalOperator left, AST right) {
        final LogicalOperator otherInput = apply(right);
        final Schema outputSchema = joinOutputSchema(left.schema(), otherInput.schema());

        return new CartesianProductLop(left, otherInput, outputSchema);
    }

    @Override
    public LogicalOperator visit(Values values) {
        final List<List<ValueExpression>> matrix = new ArrayList<>();

        for (Row row : values.rows()) {
            final List<AST> expressions = row.values();
            final List<ValueExpression> valueExpressions = new ArrayList<>();

            for (AST expression : expressions) {
                schemaValidator(EMPTY_SCHEMA).apply(expression);

                valueExpressions.add(createValueExpression(expression, EMPTY_SCHEMA));
            }

            matrix.add(valueExpressions);
        }

        return new TableConstructorLop(matrix);
    }

    private LogicalOperator mergeInputWithSubqueries(LogicalOperator input, List<AST> subqueries) {
        if (subqueries.isEmpty()) {
            return input;
        } else {
            LogicalOperator mergedInput = input;

            for (AST subquery : subqueries) {
                mergedInput = cartesianProduct(mergedInput, subquery);
            }

            return mergedInput;
        }
    }

    @Override
    public LogicalOperator defaultVisit(AST node) {
        throw new IllegalStateException();
    }

    private static Schema joinOutputSchema(Schema leftSchema, Schema rightSchema) {
        return leftSchema
                .extend(rightSchema.columnNames().stream().map(rightSchema::column).collect(toList()));
    }
}

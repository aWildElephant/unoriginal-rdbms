package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.GroupBy;
import fr.awildelephant.rdbms.ast.InnerJoin;
import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.SortedSelect;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.TableReferenceList;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.Where;
import fr.awildelephant.rdbms.engine.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.CartesianProductLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.InnerJoinLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.TableConstructorLop;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fr.awildelephant.rdbms.algebraizer.ASTToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.algebraizer.OutputColumnsTransformer.transformOutputColumns;
import static fr.awildelephant.rdbms.engine.data.table.system.NothingSystemTable.EMPTY_SCHEMA;

public final class Algebraizer extends DefaultASTVisitor<LogicalOperator> {

    private final Storage storage;

    public Algebraizer(Storage storage) {
        this.storage = storage;
    }

    @Override
    public LogicalOperator visit(Distinct distinct) {
        return new DistinctLop(apply(distinct.input()));
    }

    @Override
    public LogicalOperator visit(GroupBy groupBy) {
        return new BreakdownLop(apply(groupBy.input()), groupBy.groupingSpecification().breakdowns());
    }

    @Override
    public LogicalOperator visit(InnerJoin innerJoin) {
        final LogicalOperator leftInput = apply(innerJoin.leftTable());
        final LogicalOperator rightInput = apply(innerJoin.rightTable());

        final Schema outputSchema = joinOutputSchema(leftInput.schema(), rightInput.schema());

        return new InnerJoinLop(leftInput, rightInput, createFormula(innerJoin.joinSpecification(), outputSchema, null),
                                outputSchema);
    }

    @Override
    public LogicalOperator visit(SortedSelect sortedSelect) {
        return transformOutputColumns(apply(sortedSelect.inputTable()), sortedSelect.outputColumns(),
                                      sortedSelect.sorting());
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
            final LogicalOperator otherInput = apply(other);
            outputSchema = joinOutputSchema(cartesianProduct.schema(), otherInput.schema());
            cartesianProduct = new CartesianProductLop(cartesianProduct, otherInput, outputSchema);
        }

        return cartesianProduct;
    }

    @Override
    public LogicalOperator visit(Values values) {
        final List<List<Formula>> matrix = new ArrayList<>();

        for (Row row : values.rows()) {
            final List<AST> expressions = row.values();
            final List<Formula> formulas = new ArrayList<>();

            for (int i = 0; i < expressions.size(); i++) {
                formulas.add(createFormula(expressions.get(i), EMPTY_SCHEMA, "column" + (i + 1)));
            }

            matrix.add(formulas);
        }

        return new TableConstructorLop(matrix);
    }

    @Override
    public LogicalOperator visit(Where where) {
        final LogicalOperator input = apply(where.input());

        final Formula filter = createFormula(where.filter(), input.schema(), "<filter>");

        return new FilterLop(input, filter);
    }

    @Override
    public LogicalOperator defaultVisit(AST node) {
        throw new IllegalStateException();
    }

    private static Schema joinOutputSchema(Schema leftSchema, Schema rightSchema) {
        return leftSchema
                .extend(rightSchema.columnNames().stream().map(rightSchema::column).collect(Collectors.toList()));
    }
}

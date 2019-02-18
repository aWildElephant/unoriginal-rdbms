package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.GroupBy;
import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.Where;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.TableConstructorLop;

import java.util.ArrayList;
import java.util.List;

import static fr.awildelephant.rdbms.algebraizer.ASTToFormulaTransformer.createFormula;
import static fr.awildelephant.rdbms.algebraizer.OutputColumnsTransformer.transformOutputColumns;
import static fr.awildelephant.rdbms.engine.data.table.system.NothingSystemTable.EMPTY_SCHEMA;

public final class Algebraizer extends DefaultASTVisitor<LogicalOperator> {

    private final Engine engine;

    public Algebraizer(Engine engine) {
        this.engine = engine;
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
    public LogicalOperator visit(Select select) {
        return transformOutputColumns(apply(select.inputTable()), select.outputColumns());
    }

    @Override
    public LogicalOperator visit(TableName tableReference) {
        final String name = tableReference.name();
        final Table table = engine.get(name);

        return new BaseTableLop(name, table.schema());
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
}

package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.GroupBy;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.BaseTableLop;
import fr.awildelephant.rdbms.plan.BreakdownLop;
import fr.awildelephant.rdbms.plan.DistinctLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;

import static fr.awildelephant.rdbms.algebraizer.OutputColumnsTransformer.transformOutputColumns;

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
    public LogicalOperator defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

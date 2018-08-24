package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.DistinctNode;
import fr.awildelephant.rdbms.plan.Plan;

import static fr.awildelephant.rdbms.algebraizer.OutputColumnsTransformer.transformOutputColumns;

public final class Algebraizer extends DefaultASTVisitor<Plan> {

    private final Engine engine;

    public Algebraizer(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Plan visit(Distinct distinct) {
        return new DistinctNode(apply(distinct.input()));
    }

    @Override
    public Plan visit(Select select) {
        return transformOutputColumns(apply(select.inputTable()), select.outputColumns());
    }

    @Override
    public Plan visit(TableName tableReference) {
        final String name = tableReference.name();
        final Table table = engine.get(name);

        return new BaseTable(name, table.schema());

    }

    @Override
    public Plan defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

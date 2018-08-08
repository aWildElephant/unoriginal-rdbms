package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.Distinct;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.Projection;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class Algebraizer extends DefaultASTVisitor<Plan> {

    private final Engine engine;

    public Algebraizer(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Plan visit(Select select) {
        final Plan fromClause = visit(select.inputTable());

        final List<String> outputColumns = select
                .outputColumns()
                .stream()
                .flatMap(new ColumnNameResolver(fromClause.schema()))
                .collect(toList());

        final Plan output = new Projection(outputColumns, fromClause);

        if (!select.distinct()) {
            return output;
        }

        return new Distinct(output);
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

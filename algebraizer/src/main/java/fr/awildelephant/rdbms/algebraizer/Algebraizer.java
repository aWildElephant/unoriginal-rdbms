package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Select;
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
        final String inputTable = select.inputTable().name();
        final Table table = engine.get(inputTable);

        final BaseTable fromClause = new BaseTable(inputTable, table.schema());

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
    public Plan defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

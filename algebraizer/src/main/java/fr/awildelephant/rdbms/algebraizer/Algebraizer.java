package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.Projection;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Algebraizer extends DefaultASTVisitor<Plan> {

    @Override
    public Plan visit(Select select) {
        final Set<String> outputColumns = select
                .outputColumns()
                .stream()
                .map(new ColumnNameResolver())
                .collect(toSet());
        final String inputTable = select.inputTable().name();

        return new Projection(outputColumns, new BaseTable(inputTable));
    }

    @Override
    public Plan defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

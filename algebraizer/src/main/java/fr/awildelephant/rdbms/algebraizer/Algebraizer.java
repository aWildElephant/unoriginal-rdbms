package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.engine.Engine;
import fr.awildelephant.rdbms.plan.BaseTable;
import fr.awildelephant.rdbms.plan.Plan;
import fr.awildelephant.rdbms.plan.Projection;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class Algebraizer extends DefaultASTVisitor<Plan> {

    private final Engine engine;

    public Algebraizer(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Plan visit(Select select) {
        final String inputTable = select.inputTable().name();
        final Optional<Schema> schema = engine.schemaOf(inputTable);

        if (!schema.isPresent()) {
            throw new IllegalArgumentException("Table not found: " + inputTable);
        }

        final BaseTable fromClause = new BaseTable(inputTable, schema.get());

        final Set<String> outputColumns = select
                .outputColumns()
                .stream()
                .map(new ColumnNameResolver(fromClause.schema()))
                .collect(toSet());


        return new Projection(fromClause);
    }

    @Override
    public Plan defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

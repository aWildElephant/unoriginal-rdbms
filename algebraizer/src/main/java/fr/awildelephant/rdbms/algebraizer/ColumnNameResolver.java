package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.schema.Schema;

public class ColumnNameResolver extends DefaultASTVisitor<String> {

    private final Schema inputSchema;

    ColumnNameResolver(Schema inputSchema) {
        this.inputSchema = inputSchema;
    }

    @Override
    public String visit(ColumnName columnName) {
        final String name = columnName.name();

        if (!inputSchema.contains(name)) {
            throw new IllegalArgumentException("Column not found: " + name);
        }

        return name;
    }

    @Override
    public String defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;

public class ColumnNameResolver extends DefaultASTVisitor<String> {

    @Override
    public String visit(ColumnName columnName) {
        return columnName.name();
    }

    @Override
    public String defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

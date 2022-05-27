package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;

public final class FirstColumnNameResolver extends DefaultASTVisitor<String> {

    private final ColumnNameResolver columnNameResolver;

    public FirstColumnNameResolver(ColumnNameResolver columnNameResolver) {
        this.columnNameResolver = columnNameResolver;
    }

    @Override
    public String visit(Select select) {
        return columnNameResolver.apply(select.outputColumns().get(0));
    }

    @Override
    public String visit(Values values) {
        return "column1";
    }

    @Override
    public String defaultVisit(AST node) {
        throw new IllegalStateException();
    }
}

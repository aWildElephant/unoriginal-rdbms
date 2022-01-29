package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

public record TableAliasWithColumns(AST input, String tableAlias, List<String> columnAliases) implements AST {

    public static TableAliasWithColumns tableAliasWithColumns(AST input, String tableAlias, List<String> columnAliases) {
        return new TableAliasWithColumns(input, tableAlias, columnAliases);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

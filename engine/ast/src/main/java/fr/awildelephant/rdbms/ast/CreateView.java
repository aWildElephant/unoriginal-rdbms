package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

public record CreateView(String name, List<String> columnNames, AST query) implements AST {

    public static CreateView createView(String name, List<String> columnNames, AST query) {
        return new CreateView(name, columnNames, query);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

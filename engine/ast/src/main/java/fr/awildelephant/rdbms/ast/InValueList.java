package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

public record InValueList(List<AST> values) implements AST {

    public static InValueList inValueList(List<AST> values) {
        return new InValueList(values);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

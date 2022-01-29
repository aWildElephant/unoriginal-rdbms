package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record TableReferenceList(AST first, AST second, List<AST> others) implements AST {

    public static TableReferenceList tableReferenceList(AST first, AST second, List<AST> others) {
        return new TableReferenceList(first, second, others);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("first", first)
                .append("second", second)
                .append("others", others)
                .toString();
    }
}

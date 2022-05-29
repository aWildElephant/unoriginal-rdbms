package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.BinaryNode;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Like extends BinaryNode<AST, AST, AST> implements AST {

    private Like(AST input, AST pattern) {
        super(input, pattern);
    }

    public static Like like(AST input, AST pattern) {
        return new Like(input, pattern);
    }

    public AST input() {
        return firstChild();
    }

    public AST pattern() {
        return secondChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", firstChild())
                .append("pattern", secondChild())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Like other)) {
            return false;
        }

        return equalsBinaryNode(other);
    }
}

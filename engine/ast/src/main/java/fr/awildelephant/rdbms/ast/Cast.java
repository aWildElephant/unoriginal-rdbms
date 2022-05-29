package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.Objects;

public class Cast extends UnaryNode<AST, AST> implements AST {

    private final ColumnType targetType;

    public Cast(AST child, ColumnType targetType) {
        super(child);
        this.targetType = targetType;
    }

    public static Cast cast(final AST child, final ColumnType targetType) {
        return new Cast(child, targetType);
    }

    public ColumnType targetType() {
        return targetType;
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cast other)) {
            return false;
        }

        return Objects.equals(targetType, other.targetType)
                && equalsUnaryNode(other);
    }
}

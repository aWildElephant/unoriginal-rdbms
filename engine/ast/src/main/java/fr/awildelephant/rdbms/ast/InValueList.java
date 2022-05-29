package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.NAryNode;

import java.util.List;

public final class InValueList extends NAryNode<AST, AST> implements AST {

    public InValueList(List<AST> values) {
        super(values);
    }

    public static InValueList inValueList(List<AST> values) {
        return new InValueList(values);
    }

    public List<AST> values() {
        return children();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "InValueList[values=" + children() + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InValueList other)) {
            return false;
        }

        return equalsNAry(other);
    }
}

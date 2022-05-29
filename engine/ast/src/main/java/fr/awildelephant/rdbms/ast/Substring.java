package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.TernaryNode;

public final class Substring extends TernaryNode<AST, AST, AST, AST> implements AST {

    public Substring(AST input, AST start, AST length) {
        super(input, start, length);
    }

    public static Substring substring(AST input, AST start, AST length) {
        return new Substring(input, start, length);
    }

    public AST input() {
        return firstChild();
    }

    public AST start() {
        return secondChild();
    }

    public AST length() {
        return thirdChild();
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Substring[" +
                "input=" + firstChild() + ", " +
                "start=" + secondChild() + ", " +
                "length=" + thirdChild() + ']';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Substring other)) {
            return false;
        }
        return equalsTernary(other);
    }
}

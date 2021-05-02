package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;

public final class InValueList implements AST {

    private final List<AST> values;

    private InValueList(List<AST> values) {
        this.values = values;
    }

    public static InValueList inValueList(List<AST> values) {
        return new InValueList(values);
    }

    public List<AST> values() {
        return values;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InValueList)) {
            return false;
        }

        final InValueList other = (InValueList) obj;

        return Objects.equals(values, other.values);
    }
}

package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Collection;
import java.util.Objects;

public final class In implements AST {

    private final AST input;
    private final Collection<AST> values;

    private In(AST input, Collection<AST> values) {
        this.input = input;
        this.values = values;
    }

    public static In in(AST input, Collection<AST> values) {
        return new In(input, values);
    }

    public AST input() {
        return input;
    }

    public Collection<AST> values() {
        return values;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof In)) {
            return false;
        }

        final In other = (In) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(values, other.values);
    }
}

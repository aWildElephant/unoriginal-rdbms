package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class Where implements AST {

    private final AST input;
    private final AST filter;

    private Where(AST input, AST filter) {
        this.input = input;
        this.filter = filter;
    }

    public static AST where(AST input, AST filter) {
        return new Where(input, filter);
    }

    public AST input() {
        return input;
    }

    public AST filter() {
        return filter;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, filter);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Where)) {
            return false;
        }

        final Where other = (Where) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(filter, other.filter);
    }
}

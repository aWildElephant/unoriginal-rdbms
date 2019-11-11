package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class Having implements AST {

    private final AST input;
    private final AST filter;

    private Having(AST input, AST filter) {
        this.input = input;
        this.filter = filter;
    }

    public static Having having(AST input, AST filter) {
        return new Having(input, filter);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public AST input() {
        return input;
    }

    public AST filter() {
        return filter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, filter);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Having)) {
            return false;
        }

        final Having other = (Having) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(filter, other.filter);
    }
}

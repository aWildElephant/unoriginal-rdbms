package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class Interval implements AST {

    private final String intervalString;
    private final int precision;

    private Interval(String intervalString, int precision) {
        this.intervalString = intervalString;
        this.precision = precision;
    }

    public static Interval interval(String intervalString, int precision) {
        return new Interval(intervalString, precision);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intervalString, precision);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Interval)) {
            return false;
        }

        final Interval other = (Interval) obj;

        return Objects.equals(intervalString, other.intervalString)
                && precision == other.precision;
    }
}

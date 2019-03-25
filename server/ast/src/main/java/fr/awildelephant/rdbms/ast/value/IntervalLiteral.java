package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class IntervalLiteral implements AST {

    private final String intervalString;
    private final Integer precision;

    private IntervalLiteral(String intervalString, Integer precision) {
        this.intervalString = intervalString;
        this.precision = precision;
    }

    public static IntervalLiteral intervalLiteral(String intervalString, Integer precision) {
        return new IntervalLiteral(intervalString, precision);
    }

    public String intervalString() {
        return intervalString;
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
        if (!(obj instanceof IntervalLiteral)) {
            return false;
        }

        final IntervalLiteral other = (IntervalLiteral) obj;

        return Objects.equals(intervalString, other.intervalString)
                && Objects.equals(precision, other.precision);
    }
}

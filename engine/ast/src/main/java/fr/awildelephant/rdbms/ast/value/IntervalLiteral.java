package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.LeafNode;

import java.util.Objects;

public final class IntervalLiteral extends LeafNode<AST> implements AST {

    private final String intervalString;
    private final IntervalGranularity granularity;
    private final Integer precision;

    private IntervalLiteral(String intervalString, IntervalGranularity granularity, Integer precision) {
        this.intervalString = intervalString;
        this.granularity = granularity;
        this.precision = precision;
    }

    public static IntervalLiteral intervalLiteral(String intervalString, IntervalGranularity granularity, Integer precision) {
        return new IntervalLiteral(intervalString, granularity, precision);
    }

    public String intervalString() {
        return intervalString;
    }

    public IntervalGranularity granularity() {
        return granularity;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intervalString, granularity, precision);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final IntervalLiteral other)) {
            return false;
        }

        return granularity == other.granularity
                && Objects.equals(intervalString, other.intervalString)
                && Objects.equals(precision, other.precision);
    }
}

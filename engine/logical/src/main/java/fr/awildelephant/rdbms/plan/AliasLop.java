package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.alias.Alias;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class AliasLop extends AbstractLop {

    private final LogicalOperator input;
    private final Alias alias;

    public AliasLop(LogicalOperator input, Alias alias) {
        super(input.schema().alias(alias::alias));

        this.input = input;
        this.alias = alias;
    }

    public LogicalOperator input() {
        return input;
    }

    public Alias alias() {
        return alias;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return new AliasLop(transformer.apply(input), alias);
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final AliasLop other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(alias, other.alias);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("alias", alias)
                .toString();
    }
}

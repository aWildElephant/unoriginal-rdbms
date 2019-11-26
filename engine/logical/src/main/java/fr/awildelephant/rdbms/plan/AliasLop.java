package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.plan.alias.Alias;

import java.util.Objects;
import java.util.function.Function;

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
        if (!(obj instanceof AliasLop)) {
            return false;
        }

        final AliasLop other = (AliasLop) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(alias, other.alias);
    }
}

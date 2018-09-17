package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Alias;

public final class AliasLop extends AbstractLop {

    private final LogicalOperator input;

    private AliasLop(Alias alias, LogicalOperator input) {
        super(input.schema().alias(alias));

        this.input = input;
    }

    public static AliasLop aliasOperator(Alias alias, LogicalOperator input) {
        return new AliasLop(alias, input);
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Alias;

public final class AliasNode extends AbstractPlan {

    private final Plan input;

    private AliasNode(Alias alias, Plan input) {
        super(input.schema().alias(alias));

        this.input = input;
    }

    public static AliasNode aliasOperator(Alias alias, Plan input) {
        return new AliasNode(alias, input);
    }

    public Plan input() {
        return input;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class AggregationNode extends AbstractPlan {

    private final Plan input;

    public AggregationNode(Plan input) {
        super(buildOutputSchema());

        this.input = input;
    }

    private static Schema buildOutputSchema() {
        return new Schema(List.of(new Column(0, "COUNT(*)", INTEGER, true)));
    }

    public Plan input() {
        return input;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

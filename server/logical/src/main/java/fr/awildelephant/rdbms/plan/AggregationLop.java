package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.List;

import static fr.awildelephant.rdbms.schema.Domain.INTEGER;

public class AggregationLop extends AbstractLop {

    private final LogicalOperator input;

    public AggregationLop(LogicalOperator input) {
        super(buildOutputSchema());

        this.input = input;
    }

    private static Schema buildOutputSchema() {
        return new Schema(List.of(new Column(0, "count(*)", INTEGER, true)));
    }

    public LogicalOperator input() {
        return input;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

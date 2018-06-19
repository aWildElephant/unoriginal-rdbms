package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.HashMap;
import java.util.Set;

public class Projection extends AbstractPlan {

    private final Plan input;

    public Projection(Set<String> outputColumns, Plan input) {
        super(buildSchema(outputColumns));
        this.input = input;
    }

    private static Schema buildSchema(Set<String> outputColumns) {
        final HashMap<String, Integer> mapping = new HashMap<>();

        int i = 0;

        for (String column : outputColumns) {
            mapping.put(column, i);

            i++;
        }

        return new Schema(mapping);
    }

    public Plan input() {
        return input;
    }

    @Override
    public <T> T accept(PlanVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

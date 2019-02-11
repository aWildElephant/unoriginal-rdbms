package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.evaluator.Formula;
import fr.awildelephant.rdbms.schema.Column;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.List;

public class ExplicitTableLop extends AbstractLop {

    private final List<List<Formula>> matrix;

    public ExplicitTableLop(List<List<Formula>> matrix) {
        super(createSchema(matrix));

        this.matrix = matrix;
    }

    private static Schema createSchema(List<List<Formula>> matrix) {
        final ArrayList<Column> columns = new ArrayList<>();
        final List<Formula> firstRow = matrix.get(0);

        for (int i = 0; i < firstRow.size(); i++) {
            final Formula formula = firstRow.get(i);

            // TODO: try to determine whether or not the formula is nullable
            columns.add(new Column(i, formula.outputName(), formula.outputType(), true));
        }

        return new Schema(columns);
    }

    public List<List<Formula>> matrix() {
        return matrix;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }
}

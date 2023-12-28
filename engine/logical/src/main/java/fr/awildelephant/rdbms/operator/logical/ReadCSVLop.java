package fr.awildelephant.rdbms.operator.logical;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;
import java.util.function.Function;

public final class ReadCSVLop extends AbstractLop {

    private final String filePath;

    public ReadCSVLop(final String filePath, final Schema schema) {
        super(schema);

        this.filePath = filePath;
    }

    public String filePath() {
        return filePath;
    }

    @Override
    public LogicalOperator transformInputs(Function<LogicalOperator, LogicalOperator> transformer) {
        return this;
    }

    @Override
    public <T> T accept(LopVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(filePath);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ReadCSVLop other)) {
            return false;
        }

        return Objects.equals(filePath, other.filePath);
    }
}

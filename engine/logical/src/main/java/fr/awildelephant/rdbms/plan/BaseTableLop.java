package fr.awildelephant.rdbms.plan;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class BaseTableLop extends AbstractLop {

    private final String name;

    public BaseTableLop(String name, Schema schema) {
        super(schema);

        this.name = name;
    }

    public String name() {
        return name;
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
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BaseTableLop)) {
            return false;
        }

        final BaseTableLop other = (BaseTableLop) obj;

        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(name)
                .toString();
    }
}

package fr.awildelephant.rdbms.operator.logical;

import fr.awildelephant.rdbms.schema.Schema;
import fr.awildelephant.rdbms.version.Version;

import java.util.Objects;
import java.util.function.Function;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class BaseTableLop extends AbstractLop {

    private final String name;
    private final Version version;

    public BaseTableLop(String name, Schema schema, Version version) {
        super(schema);

        this.name = name;
        this.version = version;
    }

    public String name() {
        return name;
    }

    public Version version() {
        return version;
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
        return Objects.hash(name, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final BaseTableLop other)) {
            return false;
        }

        return Objects.equals(name, other.name)
                && Objects.equals(version, other.version);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(name)
                .append(version)
                .toString();
    }
}

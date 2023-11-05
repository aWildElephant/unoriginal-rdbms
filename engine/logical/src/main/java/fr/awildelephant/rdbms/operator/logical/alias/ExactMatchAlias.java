package fr.awildelephant.rdbms.operator.logical.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.Map;
import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ExactMatchAlias implements ReversibleAlias {

    private final Map<ColumnReference, ColumnReference> aliasing;

    public ExactMatchAlias(Map<ColumnReference, ColumnReference> aliasing) {
        this.aliasing = aliasing;
    }

    @Override
    public ColumnReference alias(ColumnReference reference) {
        final ColumnReference alias = aliasing.get(reference);
        return alias != null ? alias : reference;
    }

    @Override
    public ColumnReference unalias(ColumnReference reference) {
        for (Map.Entry<ColumnReference, ColumnReference> aliasingEntry : aliasing.entrySet()) {
            if (reference.equals(aliasingEntry.getValue())) {
                return aliasingEntry.getKey();
            }
        }

        return reference;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(aliasing);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ExactMatchAlias other)) {
            return false;
        }

        return Objects.equals(aliasing, other.aliasing);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("aliasing", aliasing)
                .toString();
    }
}

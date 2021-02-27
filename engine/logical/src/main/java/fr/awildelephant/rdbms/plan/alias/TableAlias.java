package fr.awildelephant.rdbms.plan.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class TableAlias implements Alias {

    private final String source;
    private final String alias;

    private TableAlias(String source, String alias) {
        this.source = source;
        this.alias = alias;
    }

    public static TableAlias tableAlias(String source, String alias) {
        return new TableAlias(source, alias);
    }

    @Override
    public ColumnReference alias(ColumnReference reference) {
        return new QualifiedColumnReference(alias, reference.name());
    }

    @Override
    public ColumnReference unalias(ColumnReference reference) {
        if (!reference.table().isPresent()) {
            return reference;
        }

        final String tableName = reference.table().get();

        if (!tableName.equals(alias)) {
            return reference;
        }

        return source != null
                ? new QualifiedColumnReference(source, reference.name())
                : new UnqualifiedColumnReference(reference.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableAlias)) {
            return false;
        }

        final TableAlias other = (TableAlias) obj;

        return Objects.equals(source, other.source)
                && Objects.equals(alias, other.alias);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("source", source)
                .append("alias", alias)
                .toString();
    }
}

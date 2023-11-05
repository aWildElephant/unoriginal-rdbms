package fr.awildelephant.rdbms.operator.logical.alias;

import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.operator.logical.alias.TableAlias.tableAlias;
import static org.assertj.core.api.Assertions.assertThat;

class TableAliasTest {

    private static final TableAlias ALIAS = tableAlias("source", "alias");

    @Test
    void it_should_modify_an_unqualified_column_reference() {
        final ColumnReference columnReference = new UnqualifiedColumnReference("a");

        assertThat(ALIAS.alias(columnReference)).isEqualTo(new QualifiedColumnReference("alias", "a"));
    }

    @Test
    void it_should_modify_a_qualified_column_reference_from_the_aliased_table() {
        final ColumnReference columnReferenceToAlias = new QualifiedColumnReference("original", "a");

        assertThat(ALIAS.alias(columnReferenceToAlias)).isEqualTo(new QualifiedColumnReference("alias", "a"));
    }
}
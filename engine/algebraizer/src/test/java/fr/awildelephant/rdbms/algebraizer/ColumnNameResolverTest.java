package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.value.Sum;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.BooleanLiteral.TRUE;
import static fr.awildelephant.rdbms.ast.value.Sum.sum;
import static org.assertj.core.api.Assertions.assertThat;

class ColumnNameResolverTest {

    @Test
    void it_should_resolve_the_name_of_a_sum() {
        final ColumnNameResolver resolver = new ColumnNameResolver();

        final Sum input = sum(unqualifiedColumnName("x"));

        assertThat(resolver.apply(input)).isEqualTo("sum(x)");
    }

    @Test
    void it_should_resolve_true() {
        final ColumnNameResolver resolver = new ColumnNameResolver();

        assertThat(resolver.apply(TRUE)).isEqualTo("true");
    }
}
package fr.awildelephant.rdbms.operator.logical.arithmetic;

import fr.awildelephant.rdbms.arithmetic.AndExpression;
import fr.awildelephant.rdbms.arithmetic.EqualExpression;
import fr.awildelephant.rdbms.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.Domain;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;
import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.arithmetic.AndExpression.andExpression;
import static fr.awildelephant.rdbms.arithmetic.EqualExpression.equalExpression;
import static fr.awildelephant.rdbms.arithmetic.Variable.variable;
import static fr.awildelephant.rdbms.arithmetic.optimization.FilterExpander.expandFilters;
import static org.assertj.core.api.Assertions.assertThat;

class FilterExpanderTest {

    @Test
    void it_should_transform_an_and_expression_to_two_expressions() {
        final Variable a = variable(new UnqualifiedColumnReference("a"), Domain.INTEGER);
        final Variable b = variable(new UnqualifiedColumnReference("b"), Domain.INTEGER);
        final Variable c = variable(new UnqualifiedColumnReference("c"), Domain.INTEGER);

        final EqualExpression leftFilter = equalExpression(a, b);
        final EqualExpression rightFilter = equalExpression(b, c);

        final AndExpression bigFilter = andExpression(leftFilter, rightFilter);

        assertThat(expandFilters(bigFilter)).containsExactly(leftFilter, rightFilter);
    }
}
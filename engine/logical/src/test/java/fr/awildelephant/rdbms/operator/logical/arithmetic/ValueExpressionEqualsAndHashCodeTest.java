package fr.awildelephant.rdbms.operator.logical.arithmetic;

import fr.awildelephant.rdbms.arithmetic.ConstantExpression;
import fr.awildelephant.rdbms.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.arithmetic.Variable;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

class ValueExpressionEqualsAndHashCodeTest {

    // TODO: we're not testing more classes because of a reflection issue in equalsverifier
    private static final Set<Class<?>> TO_TEST = Set.of(ConstantExpression.class, OuterQueryVariable.class, Variable.class);

    private static Collection<Arguments> parameters() {
        return implementationsOfExpression()
                .map(type -> Arguments.of(Named.named(type.getSimpleName(), type)))
                .toList();
    }

    private static Stream<Class<? extends ValueExpression>> implementationsOfExpression() {
        return new Reflections("fr.awildelephant.rdbms.arithmetic")
                .getSubTypesOf(ValueExpression.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(TO_TEST::contains);
    }

    @DisplayName("Test equals and hashCode for some implementations of ValueExpression")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void it_should_implement_equals_and_hashCode(Class<? extends ValueExpression> implementationOfExpression) {
        EqualsVerifier.forClass(implementationOfExpression).verify();

    }
}

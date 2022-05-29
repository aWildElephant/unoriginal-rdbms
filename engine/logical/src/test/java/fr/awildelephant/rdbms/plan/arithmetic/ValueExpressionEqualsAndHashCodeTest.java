package fr.awildelephant.rdbms.plan.arithmetic;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class ValueExpressionEqualsAndHashCodeTest {

    private static final Set<Class<?>> TO_TEST = Set.of(ConstantExpression.class, OuterQueryVariable.class, Variable.class);

    private static Collection<Object[]> parameters() {
        return implementationsOfExpression()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends ValueExpression>> implementationsOfExpression() {
        return new Reflections("fr.awildelephant.rdbms.plan.arithmetic")
                .getSubTypesOf(ValueExpression.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(TO_TEST::contains);
    }

    @DisplayName("All implementations of ValueExpression should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_ValueExpression_should_implement_equals_and_hashCode(String className, Class<? extends ValueExpression> implementationOfExpression) {
        EqualsVerifier.forClass(implementationOfExpression).verify();

    }
}

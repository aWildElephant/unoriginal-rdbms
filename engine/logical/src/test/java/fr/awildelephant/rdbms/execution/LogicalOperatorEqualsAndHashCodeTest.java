package fr.awildelephant.rdbms.execution;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

class LogicalOperatorEqualsAndHashCodeTest {

    private static Collection<Arguments> parameters() {
        return implementationsOfLogicalOperator()
                .map(type -> Arguments.of(Named.named(type.getSimpleName(), type)))
                .toList();
    }

    private static Stream<Class<? extends LogicalOperator>> implementationsOfLogicalOperator() {
        return new Reflections("fr.awildelephant.rdbms.execution")
                .getSubTypesOf(LogicalOperator.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(type -> !AbstractLop.class.equals(type)
                        && !AbstractBinaryLop.class.equals(type));
    }

    @DisplayName("All implementations of LogicalOperator should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_LogicalOperator_should_implement_equals_and_hashCode(Class<? extends LogicalOperator> implementationOfLogicalOperator) {
        EqualsVerifier.forClass(implementationOfLogicalOperator).withIgnoredFields("schema").verify();
    }
}

package fr.awildelephant.rdbms.operator.logical.aggregation;

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

class AggregateEqualsAndHashCodeTest {

    private static Collection<Arguments> parameters() {
        return implementationsOfAggregate()
                .map(type -> Arguments.of(Named.named(type.getSimpleName(), type)))
                .toList();
    }

    private static Stream<Class<? extends Aggregate>> implementationsOfAggregate() {
        return new Reflections("fr.awildelephant.rdbms.operator.logical.aggregation")
                .getSubTypesOf(Aggregate.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(type -> !AbstractAggregate.class.equals(type));
    }

    @DisplayName("All implementations of Aggregate should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_Aggregate_should_implement_equals_and_hashCode(Class<? extends Aggregate> implementationOfAggregate) {
        EqualsVerifier.forClass(implementationOfAggregate).verify();
    }
}

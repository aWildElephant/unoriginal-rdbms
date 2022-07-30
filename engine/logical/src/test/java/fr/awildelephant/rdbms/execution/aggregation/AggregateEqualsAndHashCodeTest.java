package fr.awildelephant.rdbms.execution.aggregation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Disabled
class AggregateEqualsAndHashCodeTest {

    private static Collection<Object[]> parameters() {
        return implementationsOfAggregate()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends Aggregate>> implementationsOfAggregate() {
        return new Reflections("fr.awildelephant.rdbms.plan.aggregation")
                .getSubTypesOf(Aggregate.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(type -> !AbstractAggregate.class.equals(type));
    }

    @DisplayName("All implementations of Aggregate should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_Aggregate_should_implement_equals_and_hashCode(String className, Class<? extends Aggregate> implementationOfAggregate) {
        EqualsVerifier.forClass(implementationOfAggregate).verify();
    }
}

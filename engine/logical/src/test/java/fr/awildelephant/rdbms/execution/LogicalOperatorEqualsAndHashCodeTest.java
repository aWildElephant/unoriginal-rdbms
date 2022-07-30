package fr.awildelephant.rdbms.execution;

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
class LogicalOperatorEqualsAndHashCodeTest {

    private static Collection<Object[]> parameters() {
        return implementationsOfLogicalOperator()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends LogicalOperator>> implementationsOfLogicalOperator() {
        return new Reflections("fr.awildelephant.rdbms.plan")
                .getSubTypesOf(LogicalOperator.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(type -> !AbstractLop.class.equals(type)
                        && !AbstractBinaryLop.class.equals(type));
    }

    @DisplayName("All implementations of LogicalOperator should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_LogicalOperator_should_implement_equals_and_hashCode(String className,
                                                                                     Class<? extends LogicalOperator> implementationOfLogicalOperator) {
        EqualsVerifier.forClass(implementationOfLogicalOperator).withIgnoredFields("schema").verify();
    }
}

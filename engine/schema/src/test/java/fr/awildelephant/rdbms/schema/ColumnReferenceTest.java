package fr.awildelephant.rdbms.schema;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class ColumnReferenceTest {

    private static Collection<Object[]> parameters() {
        return implementationsOfColumnName()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends ColumnReference>> implementationsOfColumnName() {
        return new Reflections("fr.awildelephant.rdbms.schema")
                .getSubTypesOf(ColumnReference.class)
                .stream()
                .sorted(comparing(Class::getSimpleName));
    }

    @DisplayName("All implementations of ColumnReference should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_ColumnReference_should_implement_equals_and_hashCode(String className, Class<? extends ColumnReference> implementationOfColumnName) {
        EqualsVerifier.forClass(implementationOfColumnName).verify();
    }
}

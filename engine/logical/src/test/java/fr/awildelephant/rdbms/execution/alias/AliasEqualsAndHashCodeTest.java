package fr.awildelephant.rdbms.execution.alias;

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
class AliasEqualsAndHashCodeTest {

    private static Collection<Object[]> parameters() {
        return implementationsOfAlias()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends Alias>> implementationsOfAlias() {
        return new Reflections("fr.awildelephant.rdbms.plan.alias")
                .getSubTypesOf(Alias.class)
                .stream()
                .sorted(comparing(Class::getSimpleName));
    }

    @DisplayName("All implementations of Alias should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_Alias_should_implement_equals_and_hashCode(String className, Class<? extends Alias> implementationOfAlias) {
        EqualsVerifier.forClass(implementationOfAlias).verify();
    }
}

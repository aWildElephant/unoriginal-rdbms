package fr.awildelephant.rdbms.execution.alias;

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

class AliasEqualsAndHashCodeTest {

    private static Collection<Arguments> parameters() {
        return implementationsOfAlias()
                .map(type -> Arguments.of(Named.named(type.getSimpleName(), type)))
                .toList();
    }

    private static Stream<Class<? extends Alias>> implementationsOfAlias() {
        return new Reflections("fr.awildelephant.rdbms.execution.alias")
                .getSubTypesOf(Alias.class)
                .stream()
                .sorted(comparing(Class::getSimpleName));
    }

    @DisplayName("All implementations of Alias should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_Alias_should_implement_equals_and_hashCode(Class<? extends Alias> implementationOfAlias) {
        EqualsVerifier.forClass(implementationOfAlias).verify();
    }
}

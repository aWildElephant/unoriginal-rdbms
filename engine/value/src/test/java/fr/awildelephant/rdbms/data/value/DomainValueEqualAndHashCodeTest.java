package fr.awildelephant.rdbms.data.value;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

class DomainValueEqualAndHashCodeTest {

    private static Collection<Object[]> parameters() {
        return implementationsOfDomainValue()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends DomainValue>> implementationsOfDomainValue() {
        final Set<Class<? extends DomainValue>> exceptions = Set.of(AbstractValue.class, FalseValue.class, TrueValue.class, NullValue.class);

        return new Reflections("fr.awildelephant.rdbms.data.value")
                .getSubTypesOf(DomainValue.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(type -> !exceptions.contains(type));
    }

    @DisplayName("All implementations of DomainValue should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_AST_should_implement_equals_and_hashCode(String className, Class<? extends DomainValue> implementationOfDomainValue) {
        EqualsVerifier.forClass(implementationOfDomainValue).suppress(Warning.BIGDECIMAL_EQUALITY).verify();
    }
}

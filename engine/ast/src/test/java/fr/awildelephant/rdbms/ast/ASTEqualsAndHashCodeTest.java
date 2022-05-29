package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.TextLiteral;
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

class ASTEqualsAndHashCodeTest {

    private static Collection<Object[]> parameters() {
        return implementationsOfAST()
                .map(type -> new Object[]{type.getSimpleName(), type})
                .collect(toList());
    }

    private static Stream<Class<? extends AST>> implementationsOfAST() {
        final Set<Class<? extends AST>> toTest = Set.of(
                Between.class,
                ColumnDefinition.class,
                DecimalLiteral.class,
                IntegerLiteral.class,
                IntervalLiteral.class,
                QualifiedColumnName.class,
                Select.class,
                Substring.class,
                TableName.class,
                TableReferenceList.class,
                TextLiteral.class,
                UnqualifiedColumnName.class
        );

        return new Reflections("fr.awildelephant.rdbms.ast")
                .getSubTypesOf(AST.class)
                .stream()
                .sorted(comparing(Class::getSimpleName))
                .filter(toTest::contains);
    }

    @DisplayName("All implementations of AST should implement equals and hashCode")
    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("parameters")
    void all_implementations_of_AST_should_implement_equals_and_hashCode(String className, Class<? extends AST> implementationOfAST) {
        EqualsVerifier.forClass(implementationOfAST)
                .withRedefinedSuperclass()
                .withResetCaches()
                .suppress(Warning.BIGDECIMAL_EQUALITY)
                .verify();
    }
}

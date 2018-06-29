package fr.awildelephant.rdbms.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ColumnDefinitionTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(ColumnDefinition.class).verify();
    }
}
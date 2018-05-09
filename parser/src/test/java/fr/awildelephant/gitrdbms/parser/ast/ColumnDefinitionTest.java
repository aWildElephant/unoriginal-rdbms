package fr.awildelephant.gitrdbms.parser.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ColumnDefinitionTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(ColumnDefinition.class).withNonnullFields("columnName", "columnType").verify();
    }
}
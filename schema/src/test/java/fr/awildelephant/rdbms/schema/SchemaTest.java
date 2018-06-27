package fr.awildelephant.rdbms.schema;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SchemaTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(Schema.class).verify();
    }
}
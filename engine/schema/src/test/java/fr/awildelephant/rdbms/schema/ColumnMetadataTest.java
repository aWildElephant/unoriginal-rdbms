package fr.awildelephant.rdbms.schema;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ColumnMetadataTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(ColumnMetadata.class).verify();
    }
}
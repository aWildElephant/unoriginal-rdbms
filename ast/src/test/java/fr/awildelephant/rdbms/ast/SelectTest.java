package fr.awildelephant.rdbms.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SelectTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(Select.class).withNonnullFields("outputColumns", "inputTable").verify();
    }
}

package fr.awildelephant.rdbms.engine.data.value;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class IntegerValueTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(IntegerValue.class).verify();
    }
}

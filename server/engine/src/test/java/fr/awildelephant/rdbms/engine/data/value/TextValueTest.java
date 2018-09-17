package fr.awildelephant.rdbms.engine.data.value;

import fr.awildelephant.rdbms.data.value.TextValue;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TextValueTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(TextValue.class).verify();
    }
}
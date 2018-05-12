package fr.awildelephant.gitrdbms.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class InsertIntoTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(InsertInto.class).withNonnullFields("targetTable", "rows").verify();
    }
}

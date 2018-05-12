package fr.awildelephant.gitrdbms.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class RowsTest {

    @Test
    void it_should_implements_equals_and_hashCode() {
        EqualsVerifier.forClass(Rows.class).withNonnullFields("rows").verify();
    }
}
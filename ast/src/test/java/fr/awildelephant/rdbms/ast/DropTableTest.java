package fr.awildelephant.rdbms.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class DropTableTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(DropTable.class).verify();
    }
}
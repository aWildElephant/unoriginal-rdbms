package fr.awildelephant.rdbms.ast.constraints;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UniqueConstraintTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(UniqueConstraint.class).verify();
    }
}
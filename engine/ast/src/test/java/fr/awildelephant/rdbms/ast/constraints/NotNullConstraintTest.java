package fr.awildelephant.rdbms.ast.constraints;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class NotNullConstraintTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(NotNullConstraint.class).verify();
    }
}
package fr.awildelephant.rdbms.engine.data.record;

import fr.awildelephant.rdbms.storage.data.record.Tuple;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TupleTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(Tuple.class).verify();
    }
}

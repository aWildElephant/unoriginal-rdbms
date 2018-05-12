package fr.awildelephant.gitrdbms.ast;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TableElementListTest {

    @Test
    void it_should_implement_equals_and_hashCode() {
        EqualsVerifier.forClass(TableElementList.class).withNonnullFields("elements").verify();
    }

}
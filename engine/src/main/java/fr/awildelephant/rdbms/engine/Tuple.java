package fr.awildelephant.rdbms.engine;

import fr.awildelephant.rdbms.engine.domain.DomainValue;

public class Tuple {

    private final Schema schema;
    private final DomainValue[] values;

    public Tuple(final Schema schema, final DomainValue[] values) {
        this.schema = schema;
        this.values = values;
    }

    public DomainValue get(String attributeName) {
        return get(schema.indexOf(attributeName));
    }

    public DomainValue get(int attributeIndex) {
        return values[attributeIndex];
    }
}

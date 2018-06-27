package fr.awildelephant.rdbms.engine.data.tuple;

import fr.awildelephant.rdbms.engine.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Schema;

import java.util.Arrays;
import java.util.Objects;

public final class Tuple {

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

    @Override
    public int hashCode() {
        return Objects.hashCode(schema) * 32 + Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tuple)) {
            return false;
        }

        final Tuple other = (Tuple) obj;

        return Objects.equals(schema, other.schema)
                && Arrays.equals(values, other.values);
    }
}

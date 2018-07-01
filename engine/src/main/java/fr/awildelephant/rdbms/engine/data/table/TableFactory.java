package fr.awildelephant.rdbms.engine.data.table;

import fr.awildelephant.rdbms.schema.Schema;

import java.util.ArrayList;
import java.util.HashSet;

public final class TableFactory {

    private TableFactory() {

    }

    /**
     * Returns a Table instance which can only hold distinct tuples.
     * <p>
     * Calling add with a record that already exists in the table leaves it unchanged.
     * </p>
     */
    public static Table distinctTable(Schema schema) {
        return new CollectionTable(schema, new HashSet<>());
    }

    public static Table simpleTable(Schema schema) {
        return simpleTable(schema, 8);
    }

    public static Table simpleTable(Schema schema, int initialCapacity) {
        return new CollectionTable(schema, new ArrayList<>(initialCapacity));
    }
}

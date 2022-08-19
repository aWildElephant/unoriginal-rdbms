package fr.awildelephant.rdbms.embedded;

import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;
import fr.awildelephant.rdbms.server.Glue;

public class EmbeddedServerProxy implements ServerProxy {

    private final Glue instance = new Glue();

    @Override
    public ResultProxy execute(String query) {
        final Table result = instance.execute(query);

        if (result != null) {
            return new TableProxy(result);
        } else {
            return null;
        }
    }
}

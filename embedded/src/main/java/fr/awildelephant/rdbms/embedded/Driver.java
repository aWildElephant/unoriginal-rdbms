package fr.awildelephant.rdbms.embedded;

import fr.awildelephant.rdbms.jdbc.AbstractRDBMSDriver;
import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;

public final class Driver extends AbstractRDBMSDriver {

    @Override
    protected ServerProxy createProxy(String url) {
        return new EmbeddedServerProxy();
    }

    @Override
    protected boolean acceptsNotNullURL(String url) {
        return url.startsWith("jdbc:rdbms:mem:");
    }
}

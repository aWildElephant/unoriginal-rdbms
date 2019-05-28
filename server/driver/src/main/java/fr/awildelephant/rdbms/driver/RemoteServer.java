package fr.awildelephant.rdbms.driver;

import fr.awildelephant.rdbms.jdbc.abstraction.ResultProxy;
import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;
import fr.awildelephant.rdbms.rpc.client.RPCClient;
import fr.awildelephant.rdbms.rpc.generated.Rdbms;

public class RemoteServer implements ServerProxy {

    private final RPCClient client;

    RemoteServer(String name, int port) {
        client = new RPCClient(name, port);
    }

    @Override
    public ResultProxy execute(String query) {
        final Rdbms.Result result = client.execute(query);

        if (result.hasQuery()) {
            return new RemoteResultProxy(result.getQuery());
        }

        return null;
    }
}

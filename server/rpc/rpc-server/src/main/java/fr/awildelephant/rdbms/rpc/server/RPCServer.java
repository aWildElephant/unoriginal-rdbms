package fr.awildelephant.rdbms.rpc.server;

import fr.awildelephant.rdbms.server.RDBMS;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class RPCServer {

    private final Server server;

    public RPCServer(RDBMS instance, int port) {
        server = ServerBuilder.forPort(port).addService(new RDBMSServer(instance)).build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        server.awaitTermination();
    }

    public void stop() {
        server.shutdown();
    }
}

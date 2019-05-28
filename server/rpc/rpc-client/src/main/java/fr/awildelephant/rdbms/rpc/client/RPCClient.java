package fr.awildelephant.rdbms.rpc.client;

import fr.awildelephant.rdbms.rpc.generated.RDBMSGrpc;
import fr.awildelephant.rdbms.rpc.generated.Rdbms;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RPCClient {

    private final RDBMSGrpc.RDBMSBlockingStub stub;

    public RPCClient(String name, int port) {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress(name, port).usePlaintext().build();
        stub = RDBMSGrpc.newBlockingStub(channel);
    }

    public Rdbms.Result execute(String query) {
        return stub.execute(Rdbms.Query.newBuilder().setQuery(query).build());
    }
}

package fr.awildelephant.rdbms.server.standalone;

import fr.awildelephant.rdbms.rpc.server.RPCServer;
import fr.awildelephant.rdbms.server.RDBMS;
import fr.awildelephant.rdbms.server.arguments.Arguments;

import static fr.awildelephant.rdbms.server.arguments.ArgumentsParser.parse;

public class Server {

    public static void main(String[] args) throws Exception {
        final Arguments arguments = parse(args);

        final RPCServer rpcServer = new RPCServer(new RDBMS(), arguments.port());

        Runtime.getRuntime().addShutdownHook(new Thread(rpcServer::stop));

        rpcServer.start();
    }
}

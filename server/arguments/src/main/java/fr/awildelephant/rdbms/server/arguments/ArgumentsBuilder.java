package fr.awildelephant.rdbms.server.arguments;

public final class ArgumentsBuilder {

    private int port;

    public void port(int port) {
        this.port = port;
    }

    public Arguments build() {
        return new Arguments(port);
    }
}

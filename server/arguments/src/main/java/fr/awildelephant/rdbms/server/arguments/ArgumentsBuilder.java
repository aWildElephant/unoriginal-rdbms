package fr.awildelephant.rdbms.server.arguments;

public final class ArgumentsBuilder {

    private int port;

    public ArgumentsBuilder port(int port) {
        this.port = port;

        return this;
    }

    public Arguments build() {
        return new Arguments(port);
    }
}

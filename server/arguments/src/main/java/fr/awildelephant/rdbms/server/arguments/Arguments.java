package fr.awildelephant.rdbms.server.arguments;

import java.util.Objects;

public final class Arguments {

    private final int port;

    Arguments(int port) {
        this.port = port;
    }

    public int port() {
        return port;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(port);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Arguments other)) {
            return false;
        }

        return Objects.equals(port, other.port);
    }
}

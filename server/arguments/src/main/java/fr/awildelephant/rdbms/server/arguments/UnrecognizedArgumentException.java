package fr.awildelephant.rdbms.server.arguments;

public class UnrecognizedArgumentException extends Exception {

    public UnrecognizedArgumentException(String name) {
        super("Unrecognized argument " + name);
    }
}

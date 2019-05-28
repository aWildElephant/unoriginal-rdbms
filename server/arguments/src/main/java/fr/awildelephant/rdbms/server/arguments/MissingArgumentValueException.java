package fr.awildelephant.rdbms.server.arguments;

public class MissingArgumentValueException extends Exception {

    public MissingArgumentValueException(String name) {
        super("Missing value for argument " + name);
    }
}

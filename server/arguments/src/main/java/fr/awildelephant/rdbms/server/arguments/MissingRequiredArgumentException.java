package fr.awildelephant.rdbms.server.arguments;

public class MissingRequiredArgumentException extends Exception {

    public MissingRequiredArgumentException(String name) {
        super("Missing required argument " + name);
    }
}

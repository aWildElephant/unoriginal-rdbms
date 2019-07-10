package fr.awildelephant.rdbms.tpch;

public class MissingEnvironmentVariableException extends Exception {

    MissingEnvironmentVariableException(String name) {
        super("Missing environment variable " + name);
    }
}

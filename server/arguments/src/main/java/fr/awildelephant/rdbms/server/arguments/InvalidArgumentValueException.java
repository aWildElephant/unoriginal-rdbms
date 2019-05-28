package fr.awildelephant.rdbms.server.arguments;

public class InvalidArgumentValueException extends Exception {

    public InvalidArgumentValueException(String argument, String value) {
        super(errorMessage(argument, value));
    }

    public InvalidArgumentValueException(String argument, String value, NumberFormatException cause) {
        super(errorMessage(argument, value), cause);
    }

    private static String errorMessage(String argument, String value) {
        return String.format("Invalid value '%s' for argument %s", value, argument);
    }
}

package fr.awildelephant.rdbms.server.arguments;

import static java.lang.Integer.parseInt;

public class ArgumentsParser {

    private static final String PORT_ARGUMENT = "--port";

    public static Arguments parse(String[] arguments) throws MissingRequiredArgumentException, UnrecognizedArgumentException, MissingArgumentValueException, InvalidArgumentValueException {
        final ArgumentsBuilder builder = new ArgumentsBuilder();

        if (arguments.length == 0) {
            throw new MissingRequiredArgumentException(PORT_ARGUMENT);
        }

        final String firstArgument = arguments[0];

        if (!PORT_ARGUMENT.equals(firstArgument)) {
            throw new UnrecognizedArgumentException(firstArgument);
        }

        if (arguments.length == 1) {
            throw new MissingArgumentValueException(PORT_ARGUMENT);
        }

        final String portString = arguments[1];

        try {
            final int port = parseInt(portString);

            if (port < 0 || port > 0xffff) {
                throw new InvalidArgumentValueException(PORT_ARGUMENT, portString);
            }

            builder.port(port);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentValueException(PORT_ARGUMENT, portString, e);
        }

        return builder.build();
    }
}

package fr.awildelephant.rdbms.server.arguments;

import org.junit.jupiter.api.Test;

import static fr.awildelephant.rdbms.server.arguments.ArgumentsParser.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArgumentsParserTest {

    @Test
    void it_should_throw_an_exception_if_the_port_is_missing() {
        assertThatThrownBy(ArgumentsParserTest::parseArguments).isInstanceOf(MissingRequiredArgumentException.class)
                                                               .hasMessage("Missing required argument --port");
    }

    @Test
    void it_should_throw_an_exception_if_the_value_for_the_port_is_missing() {
        assertThatThrownBy(() -> parseArguments("--port")).isInstanceOf(MissingArgumentValueException.class)
                                                          .hasMessage("Missing value for argument --port");
    }

    @Test
    void it_should_throw_an_exception_for_an_unrecognized_argument() {
        assertThatThrownBy(() -> parseArguments("--caca")).isInstanceOf(UnrecognizedArgumentException.class)
                                                          .hasMessage("Unrecognized argument --caca");
    }

    @Test
    void it_should_throw_an_exception_if_the_value_given_for_the_port_is_not_an_integer() {
        assertThatThrownBy(() -> parseArguments("--port", "caca"))
                .isInstanceOf(InvalidArgumentValueException.class)
                .hasMessage("Invalid value 'caca' for argument --port");
    }

    @Test
    void it_should_successfully_parse_the_port() throws Exception {
        final Arguments expected = new Arguments(1234);

        assertThat(parseArguments("--port", "1234")).isEqualTo(expected);
    }

    private static Arguments parseArguments(String... arguments) throws Exception {
        return parse(arguments);
    }
}

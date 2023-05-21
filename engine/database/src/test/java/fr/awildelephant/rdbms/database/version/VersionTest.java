package fr.awildelephant.rdbms.database.version;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class VersionTest {

    @ParameterizedTest
    @MethodSource("isAfterScenarios")
    void isAfter(Version reference, Version other, boolean result) {
        assertThat(reference.isAfter(other)).isEqualTo(result);
    }

    private static Stream<Arguments> isAfterScenarios() {
        return Stream.of(
                Arguments.of(new PermanentVersion(1), new PermanentVersion(0), true),
                Arguments.of(new PermanentVersion(1), new PermanentVersion(1), false),
                Arguments.of(new PermanentVersion(Long.MAX_VALUE), EndOfTimesVersion.getInstance(), false),
                Arguments.of(EndOfTimesVersion.getInstance(), new PermanentVersion(Long.MAX_VALUE), true)
        );
    }
}
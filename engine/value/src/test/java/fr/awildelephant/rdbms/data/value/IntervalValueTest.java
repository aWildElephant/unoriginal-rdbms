package fr.awildelephant.rdbms.data.value;

import org.junit.jupiter.api.Test;

import java.time.Period;

import static fr.awildelephant.rdbms.data.value.IntervalValue.intervalValue;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalValueTest {

    @Test
    void toString_should_show_5_days() {
        assertThat(intervalValue(Period.ofDays(5)).toString()).isEqualTo("IntervalValue[5D]");
    }

    @Test
    void toString_should_show_5_years() {
        assertThat(intervalValue(Period.ofYears(5)).toString()).isEqualTo("IntervalValue[5Y]");
    }
}
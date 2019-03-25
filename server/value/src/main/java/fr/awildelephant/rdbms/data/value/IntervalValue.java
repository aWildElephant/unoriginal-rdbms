package fr.awildelephant.rdbms.data.value;

import java.time.Period;
import java.util.Objects;

public final class IntervalValue extends AbstractValue {

    private final Period period;

    private IntervalValue(Period period) {
        this.period = period;
    }

    public static IntervalValue intervalValue(Period period) {
        return new IntervalValue(period);
    }

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntervalValue)) {
            return false;
        }

        final IntervalValue other = (IntervalValue) obj;

        return Objects.equals(period, other.period);
    }
}

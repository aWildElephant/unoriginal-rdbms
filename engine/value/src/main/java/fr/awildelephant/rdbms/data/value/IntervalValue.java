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

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder("IntervalValue[");

        final int years = period.getYears();
        if (years > 0) {
            stringBuilder.append(years).append('Y');
        }
        final int months = period.getMonths();
        if (months > 0) {
            stringBuilder.append(months).append('M');
        }
        final int days = period.getDays();
        if (days > 0){
            stringBuilder.append(days).append('D');
        }

        return stringBuilder.append(']').toString();
    }
}

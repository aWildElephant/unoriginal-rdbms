package fr.awildelephant.rdbms.evaluator.operation.bool.comparison;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.evaluator.operation.Operation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public final class DomainValueUtils {

    private DomainValueUtils() {

    }

    public static Integer extractInteger(final Operation operation) {
        return extractInteger(operation.evaluateAndWrap());
    }

    public static Integer extractInteger(final DomainValue domainValue) {
        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getInt();
    }

    public static BigDecimal extractBigDecimal(final Operation operation) {
        final DomainValue domainValue = operation.evaluateAndWrap();

        return extractBigDecimal(domainValue);
    }

    public static BigDecimal extractBigDecimal(DomainValue domainValue) {
        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getBigDecimal();
    }

    public static Long extractLong(final DomainValue domainValue) {
        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getLong();
    }

    public static LocalDate extractLocalDate(DomainValue domainValue) {
        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getLocalDate();
    }

    public static Period extractPeriod(DomainValue domainValue) {
        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getPeriod();
    }

    public static String extractString(DomainValue domainValue) {
        if (domainValue.isNull()) {
            return null;
        }

        return domainValue.getString();
    }

    public static ThreeValuedLogic extractThreeValuedLogic(DomainValue value) {
        if (value.isNull()) {
            return UNKNOWN;
        }
        if (value.getBool()) {
            return TRUE;
        }
        return FALSE;
    }
}

package fr.awildelephant.rdbms.utils.standard;

import java.util.EnumSet;
import java.util.Set;

import static fr.awildelephant.rdbms.utils.standard.Standards.SQL2003;
import static fr.awildelephant.rdbms.utils.standard.Standards.SQL2008;
import static fr.awildelephant.rdbms.utils.standard.Standards.SQL2011;
import static fr.awildelephant.rdbms.utils.standard.Standards.SQL92;
import static fr.awildelephant.rdbms.utils.standard.Standards.SQL99;

/**
 * List of SQL keywords in different standards.
 * <p>
 * Currently filling it with https://github.com/AlaSQL/alasql/wiki/SQL-keywords
 */
public enum Keywords {
    ABS(SQL2008, SQL2011),
    ABSOLUTE(SQL92, SQL99),
    ACTION(SQL92, SQL99), // TODO: what is that
    ADD(SQL92, SQL99),
    AFTER(SQL99), // TODO: what is that
    ALL(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    ALLOCATE(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    ALTER(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    AND(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    ANY(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    ARE(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    ARRAY(SQL99, SQL2003, SQL2008, SQL2011),
    ARRAY_AGG(SQL2008, SQL2011),
    ARRAY_MAX_CARDINALITY(SQL2011),
    AS(SQL92, SQL99, SQL2003, SQL2008, SQL2011),
    ASC(SQL92, SQL99),
    ASENSITIVE(SQL99, SQL2003, SQL2008, SQL2011),
    ASSERTION(SQL92), // TODO: supporting CREATE ASSERTION seems fun
    // TODO: the rest
    ;

    private final Set<Standards> includedIn;

    Keywords(Standards standard) {
        includedIn = EnumSet.of(standard);
    }

    Keywords(Standards first, Standards... rest) {
        includedIn = EnumSet.of(first, rest);
    }
}

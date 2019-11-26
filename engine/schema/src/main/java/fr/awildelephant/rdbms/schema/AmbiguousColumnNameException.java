package fr.awildelephant.rdbms.schema;

class AmbiguousColumnNameException extends RuntimeException {

    AmbiguousColumnNameException(String columnName) {
        super("Ambiguous column name: " + columnName);
    }
}

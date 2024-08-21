package fr.awildelephant.rdbms.evaluator.exception;

import fr.awildelephant.rdbms.error.DatabaseError;

public final class DivisionByZeroError extends DatabaseError {

    @Override
    public String getMessage() {
        return "Division by zero";
    }
}

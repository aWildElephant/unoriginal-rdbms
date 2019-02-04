package fr.awildelephant.rdbms.engine.operators;

public interface Operator<I, O> {

    O compute(I inputTable);
}

package fr.awildelephant.rdbms.execution.operator.csv;

import fr.awildelephant.rdbms.data.value.DomainValue;

import java.util.function.Function;

// TODO: handle null values for all implementations + tests
public interface CellParser extends Function<String, DomainValue> {

    DomainValue apply(String cell);
}

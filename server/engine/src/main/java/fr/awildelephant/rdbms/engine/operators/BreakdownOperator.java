package fr.awildelephant.rdbms.engine.operators;

import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.List;

public class BreakdownOperator implements Operator {

    public BreakdownOperator(List<String> breakdowns) {

    }

    @Override
    public Table compute(Table inputTable) {
        throw new UnsupportedOperationException("TODO: operator");
    }
}

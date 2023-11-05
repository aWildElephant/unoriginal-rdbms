package fr.awildelephant.rdbms.function;

import fr.awildelephant.rdbms.arithmetic.OuterQueryVariable;
import fr.awildelephant.rdbms.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.arithmetic.Variable;
import fr.awildelephant.rdbms.schema.ColumnReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class VariableCollector implements Function<ValueExpression, List<ColumnReference>> {

    @Override
    public List<ColumnReference> apply(ValueExpression valueExpression) {
        final List<ColumnReference> variables = new ArrayList<>();
        collect(valueExpression, variables);
        return variables;
    }

    public List<ColumnReference> apply(List<ValueExpression> valueExpressions) {
        final List<ColumnReference> variables = new ArrayList<>();
        valueExpressions.forEach(valueExpression -> collect(valueExpression, variables));
        return variables;
    }

    private void collect(ValueExpression valueExpression, List<ColumnReference> variables) {
        valueExpression.depthFirst(node -> {
            if (node instanceof OuterQueryVariable variable) {
                variables.add(variable.reference());
            } else if (node instanceof Variable variable) {
                variables.add(variable.reference());
            }
        });
    }
}

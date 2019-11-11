package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.List;

import static fr.awildelephant.rdbms.algebraizer.SchemaValidator.schemaValidator;
import static fr.awildelephant.rdbms.plan.arithmetic.Variable.variable;

final class HavingClauseTransformer {

    private final ColumnReferenceTransformer columnReferenceTransformer;
    private final ValuesTransformer valuesTransformer;

    HavingClauseTransformer(ColumnReferenceTransformer columnReferenceTransformer, ValuesTransformer valuesTransformer) {
        this.columnReferenceTransformer = columnReferenceTransformer;
        this.valuesTransformer = valuesTransformer;
    }

    LogicalOperator transform(LogicalOperator input, AST filter) {
        validateColumnReferences(input, filter);

        final LogicalOperator havingFormulaComputation = valuesTransformer.transform(input, List.of(filter));

        final ColumnReference filterColumnReference = columnReferenceTransformer.apply(filter);

        // TODO: check that filterColumnReference is of type boolean?

        return new FilterLop(havingFormulaComputation, variable(filterColumnReference, Domain.BOOLEAN));
    }

    private void validateColumnReferences(LogicalOperator input, AST filter) {
        schemaValidator(input.schema()).apply(filter);
    }
}

package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.plan.FilterLop;
import fr.awildelephant.rdbms.plan.LogicalOperator;
import fr.awildelephant.rdbms.plan.arithmetic.ValueExpression;
import fr.awildelephant.rdbms.schema.Schema;

import static fr.awildelephant.rdbms.algebraizer.ASTToValueExpressionTransformer.createValueExpression;
import static fr.awildelephant.rdbms.algebraizer.SchemaValidator.schemaValidator;

final class FilterTransformer {

    private final LogicalOperator input;
    private final AST filter;

    private FilterTransformer(LogicalOperator input, AST filter) {
        this.input = input;
        this.filter = filter;
    }

    static LogicalOperator transformFilter(LogicalOperator input, AST filter) {
        return new FilterTransformer(input, filter).transform();
    }

    private LogicalOperator transform() {
        final Schema inputSchema = input.schema();

        schemaValidator(inputSchema).apply(filter);

        final ValueExpression expression = createValueExpression(filter, inputSchema);

        return new FilterLop(input, expression);
    }
}

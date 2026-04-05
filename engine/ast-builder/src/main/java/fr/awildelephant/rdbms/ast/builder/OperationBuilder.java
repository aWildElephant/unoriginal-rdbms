package fr.awildelephant.rdbms.ast.builder;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.utils.common.Builder;

import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;

/**
 * A builder for any kind of binary operation.
 */
public final class OperationBuilder implements Builder<AST> {

    private final AST leftNode;
    private Operation operation;
    private AST rightNode;

    private OperationBuilder(AST leftNode) {
        this.leftNode = leftNode;
    }

    public static OperationBuilder operation(Builder<? extends AST> leftNodeBuilder) {
        return new OperationBuilder(leftNodeBuilder.build());
    }

    public OperationBuilder isLowerThanOrEqualTo(int integerValue) {
        operation = Operation.LOWER_THAN_OR_EQUAL_TO;
        rightNode = integerLiteral(integerValue);
        return this;
    }

    public AST build() {
        return switch (operation) {
            case LOWER_THAN_OR_EQUAL_TO -> lessOrEqual(leftNode, rightNode);
        };
    }

    private enum Operation {
        LOWER_THAN_OR_EQUAL_TO
    }
}

package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class InnerJoin implements AST {

    private final AST leftTable;
    private final AST rightTable;
    private final AST joinSpecification;

    private InnerJoin(AST leftTable, AST rightTable, AST joinSpecification) {
        this.leftTable = leftTable;
        this.rightTable = rightTable;
        this.joinSpecification = joinSpecification;
    }

    public static InnerJoin innerJoin(AST leftTable, AST rightTable, AST joinSpecification) {
        return new InnerJoin(leftTable, rightTable, joinSpecification);
    }

    public AST leftTable() {
        return leftTable;
    }

    public AST rightTable() {
        return rightTable;
    }

    public AST joinSpecification() {
        return joinSpecification;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftTable, rightTable, joinSpecification);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InnerJoin)) {
            return false;
        }

        final InnerJoin other = (InnerJoin) obj;

        return Objects.equals(leftTable, other.leftTable)
                && Objects.equals(rightTable, other.rightTable)
                && Objects.equals(joinSpecification, other.joinSpecification);
    }
}

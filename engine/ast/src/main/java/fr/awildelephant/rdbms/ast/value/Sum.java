package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class Sum implements AST {

    private final AST input;

    private Sum(AST input) {
        this.input = input;
    }

    public static Sum sum(AST input) {
        return new Sum(input);
    }

    public AST input() {
        return input;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Sum)) {
            return false;
        }

        final Sum other = (Sum) obj;

        return Objects.equals(input, other.input);
    }
}

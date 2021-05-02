package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

public final class ExtractYear implements AST {

    private final AST input;

    private ExtractYear(AST input) {
        this.input = input;
    }

    public static ExtractYear extractYear(AST value) {
        return new ExtractYear(value);
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
        if (!(obj instanceof ExtractYear)) {
            return false;
        }

        final ExtractYear other = (ExtractYear) obj;

        return Objects.equals(input, other.input);
    }
}

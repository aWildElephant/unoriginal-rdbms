package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class ScalarSubquery implements AST {

    private final AST input;

    private ScalarSubquery(AST input) {
        this.input = input;
    }

    public static ScalarSubquery scalarSubquery(AST input) {
        return new ScalarSubquery(input);
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
        if (!(obj instanceof ScalarSubquery)) {
            return false;
        }

        final ScalarSubquery other = (ScalarSubquery) obj;

        return Objects.equals(input, other.input);
    }
}

package fr.awildelephant.rdbms.ast.value;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.ASTVisitor;

import java.util.Objects;

public final class ScalarSubquery implements AST {

    private final AST input;
    private final String id;

    private ScalarSubquery(AST input, String id) {
        this.input = input;
        this.id = id;
    }

    public static ScalarSubquery scalarSubquery(AST input, String id) {
        return new ScalarSubquery(input, id);
    }

    public AST input() {
        return input;
    }

    public String id() {
        return id;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScalarSubquery)) {
            return false;
        }

        final ScalarSubquery other = (ScalarSubquery) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(id, other.id);
    }
}

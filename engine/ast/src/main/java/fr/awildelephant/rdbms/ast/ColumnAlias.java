package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class ColumnAlias implements AST {

    private final AST input;
    private final String alias;

    private ColumnAlias(AST input, String alias) {
        this.input = input;
        this.alias = alias;
    }

    public static ColumnAlias columnAlias(AST input, String alias) {
        return new ColumnAlias(input, alias);
    }

    public String alias() {
        return alias;
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
        return Objects.hash(input, alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnAlias)) {
            return false;
        }

        final ColumnAlias other = (ColumnAlias) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(alias, other.alias);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("alias", alias)
                .toString();
    }
}

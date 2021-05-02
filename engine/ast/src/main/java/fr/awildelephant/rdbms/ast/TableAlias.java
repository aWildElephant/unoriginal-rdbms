package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class TableAlias implements AST {

    private final AST input;
    private final String alias;

    private TableAlias(AST input, String alias) {
        this.input = input;
        this.alias = alias;
    }

    public static TableAlias tableAlias(AST input, String alias) {
        return new TableAlias(input, alias);
    }

    public AST input() {
        return input;
    }

    public String alias() {
        return alias;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("input", input)
                .append("alias", alias)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, alias);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableAlias)) {
            return false;
        }

        final TableAlias other = (TableAlias) obj;

        return Objects.equals(input, other.input)
                && Objects.equals(alias, other.alias);
    }
}

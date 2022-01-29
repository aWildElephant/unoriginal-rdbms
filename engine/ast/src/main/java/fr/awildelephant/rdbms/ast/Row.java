package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record Row(List<AST> values) implements AST {

    public static Row row(final List<AST> values) {
        return new Row(List.copyOf(values));
    }

    public static Row row(final AST... values) {
        return new Row(List.of(values));
    }

    @Override
    public <T> T accept(final ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append(values)
                .toString();
    }
}

package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Objects;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class With implements AST {

    private final WithList withList;
    private final AST query;

    private With(WithList withList, AST query) {
        this.withList = withList;
        this.query = query;
    }

    public static With with(WithList withList, AST query) {
        return new With(withList, query);
    }

    public WithList withList() {
        return withList;
    }

    public AST query() {
        return query;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(withList, query);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof With)) {
            return false;
        }

        final With other = (With) obj;

        return Objects.equals(withList, other.withList)
                && Objects.equals(query, other.query);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("withList", withList)
                .append("query", query)
                .toString();
    }
}

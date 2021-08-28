package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public final class Select implements AST {

    private final List<? extends AST> outputColumns;
    private final AST fromClause;
    private final AST whereClause;
    private final GroupingSetsList groupByClause;
    private final AST havingClause;
    private final SortSpecificationList orderByClause;

    private Select(List<? extends AST> outputColumns, AST fromClause, AST whereClause, GroupingSetsList groupByClause, AST havingClause, SortSpecificationList orderByClause) {
        this.outputColumns = outputColumns;
        this.fromClause = fromClause;
        this.whereClause = whereClause;
        this.groupByClause = groupByClause;
        this.havingClause = havingClause;
        this.orderByClause = orderByClause;
    }

    public static Select select(List<? extends AST> outputColumns, AST fromClause, AST whereClause, GroupingSetsList groupByClause, AST havingClause, SortSpecificationList orderByClause) {
        return new Select(outputColumns, fromClause, whereClause, groupByClause, havingClause, orderByClause);
    }

    public List<? extends AST> outputColumns() {
        return outputColumns;
    }

    public AST fromClause() {
        return fromClause;
    }

    public Optional<AST> whereClause() {
        return Optional.ofNullable(whereClause);
    }

    public Optional<GroupingSetsList> groupByClause() {
        return Optional.ofNullable(groupByClause);
    }

    public Optional<AST> havingClause() {
        return Optional.ofNullable(havingClause);
    }

    public Optional<SortSpecificationList> orderByClause() {
        return Optional.ofNullable(orderByClause);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("outputColumns", outputColumns)
                .append("from", fromClause)
                .append("where", whereClause)
                .append("groupBy", groupByClause)
                .append("having", havingClause)
                .append("orderBy", orderByClause)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(outputColumns, fromClause, whereClause, groupByClause, havingClause, orderByClause);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final Select other)) {
            return false;
        }

        return Objects.equals(outputColumns, other.outputColumns)
                && Objects.equals(fromClause, other.fromClause)
                && Objects.equals(whereClause, other.whereClause)
                && Objects.equals(groupByClause, other.groupByClause)
                && Objects.equals(havingClause, other.havingClause)
                && Objects.equals(orderByClause, other.orderByClause);
    }
}

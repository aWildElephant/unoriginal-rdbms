package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static fr.awildelephant.rdbms.ast.util.ToStringBuilderHelper.toStringBuilder;

public record Select(List<? extends AST> outputColumns, AST fromClause, AST whereClause, GroupingSetsList groupByClause,
                     AST havingClause, SortSpecificationList orderByClause) implements AST {

    public static Select select(List<? extends AST> outputColumns, AST fromClause, AST whereClause, GroupingSetsList groupByClause, AST havingClause, SortSpecificationList orderByClause) {
        return new Select(outputColumns, fromClause, whereClause, groupByClause, havingClause, orderByClause);
    }

    @Override
    public Collection<? extends AST> children() {
        final List<AST> children = new ArrayList<>(outputColumns);
        children.add(fromClause);
        children.add(whereClause);
        children.add(groupByClause);
        children.add(havingClause);
        children.add(orderByClause);
        return children;
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
}

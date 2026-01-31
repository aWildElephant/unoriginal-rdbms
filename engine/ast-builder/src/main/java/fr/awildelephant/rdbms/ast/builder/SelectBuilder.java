package fr.awildelephant.rdbms.ast.builder;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.utils.common.Builder;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Asterisk.asterisk;

public final class SelectBuilder implements Builder<Select> {

    private List<AST> outputColumns;
    private AST fromClause;
    private AST whereClause;
    private GroupingSetsList groupByClause;
    private AST havingClause;
    private SortSpecificationList orderByClause;

    private SelectBuilder() {

    }

    public static SelectBuilder select() {
        return new SelectBuilder();
    }

    public SelectBuilder outputColumns(AST... columns) {
        this.outputColumns = List.of(columns);
        return this;
    }

    public SelectBuilder fromClause(AST fromClause) {
        this.fromClause = fromClause;
        return this;
    }

    public SelectBuilder whereClause(AST whereClause) {
        this.whereClause = whereClause;
        return this;
    }

    public SelectBuilder groupByClause(GroupingSetsList groupByClause) {
        this.groupByClause = groupByClause;
        return this;
    }

    public SelectBuilder havingClause(AST havingClause) {
        this.havingClause = havingClause;
        return this;
    }

    public SelectBuilder orderByClause(SortSpecificationList orderByClause) {
        this.orderByClause = orderByClause;
        return this;
    }

    @Override
    public Select build() {
        return new Select(
                outputColumns == null ? List.of(asterisk()) : outputColumns,
                fromClause,
                whereClause,
                groupByClause,
                havingClause,
                orderByClause
        );
    }
}

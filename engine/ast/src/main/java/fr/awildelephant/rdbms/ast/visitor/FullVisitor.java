package fr.awildelephant.rdbms.ast.visitor;

import fr.awildelephant.rdbms.ast.*;
import fr.awildelephant.rdbms.ast.value.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.CreateView.createView;
import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.Exists.exists;
import static fr.awildelephant.rdbms.ast.Explain.explain;
import static fr.awildelephant.rdbms.ast.InValueList.inValueList;
import static fr.awildelephant.rdbms.ast.InnerJoin.innerJoin;
import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.ast.LeftJoin.leftJoin;
import static fr.awildelephant.rdbms.ast.Limit.limit;
import static fr.awildelephant.rdbms.ast.Row.row;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.SemiJoin.semiJoin;
import static fr.awildelephant.rdbms.ast.Substring.substring;
import static fr.awildelephant.rdbms.ast.TableAlias.tableAlias;
import static fr.awildelephant.rdbms.ast.TableAliasWithColumns.tableAliasWithColumns;
import static fr.awildelephant.rdbms.ast.TableReferenceList.tableReferenceList;
import static fr.awildelephant.rdbms.ast.Values.rows;
import static fr.awildelephant.rdbms.ast.With.with;
import static fr.awildelephant.rdbms.ast.WithElement.withElement;
import static fr.awildelephant.rdbms.ast.WithList.withList;
import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.Any.any;
import static fr.awildelephant.rdbms.ast.value.Avg.avg;
import static fr.awildelephant.rdbms.ast.value.Between.between;
import static fr.awildelephant.rdbms.ast.value.CaseWhen.caseWhen;
import static fr.awildelephant.rdbms.ast.value.Count.count;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.ExtractYear.extractYear;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.GreaterOrEqual.greaterOrEqual;
import static fr.awildelephant.rdbms.ast.value.In.in;
import static fr.awildelephant.rdbms.ast.value.IsNull.isNull;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;
import static fr.awildelephant.rdbms.ast.value.Like.like;
import static fr.awildelephant.rdbms.ast.value.Max.max;
import static fr.awildelephant.rdbms.ast.value.Min.min;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.NotEqual.notEqual;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;
import static fr.awildelephant.rdbms.ast.value.ScalarSubquery.scalarSubquery;
import static fr.awildelephant.rdbms.ast.value.Sum.sum;

public final class FullVisitor implements ASTVisitor<AST> {

    private final Function<AST, AST> function;

    public FullVisitor(Function<AST, AST> function) {
        this.function = function;
    }

    @Override
    public AST visit(And and) {
        return and(function.apply(and.left()), function.apply(and.right()));
    }

    @Override
    public AST visit(Any any) {
        return any(function.apply(any.input()));
    }

    @Override
    public AST visit(Asterisk asterisk) {
        return asterisk;
    }

    @Override
    public AST visit(Avg avg) {
        return avg(function.apply(avg.input()));
    }

    @Override
    public AST visit(Between between) {
        return between(function.apply(between.value()),
                function.apply(between.lowerBound()),
                function.apply(between.upperBound()));
    }

    @Override
    public AST visit(BooleanLiteral booleanLiteral) {
        return booleanLiteral;
    }

    @Override
    public AST visit(CaseWhen caseWhen) {
        return caseWhen(function.apply(caseWhen.condition()),
                function.apply(caseWhen.thenExpression()),
                function.apply(caseWhen.elseExpression()));
    }

    @Override
    public AST visit(Cast cast) {
        return cast(function.apply(cast.input()), cast.targetType());
    }

    @Override
    public AST visit(ColumnAlias columnAlias) {
        return columnAlias(function.apply(columnAlias.input()), columnAlias.alias());
    }

    @Override
    public AST visit(ColumnDefinition columnDefinition) {
        return columnDefinition;
    }

    @Override
    public AST visit(Count count) {
        return count(count.distinct(), function.apply(count.input()));
    }

    @Override
    public AST visit(CountStar countStar) {
        return countStar;
    }

    @Override
    public AST visit(CreateTable createTable) {
        return createTable;
    }

    @Override
    public AST visit(CreateView createView) {
        return createView(createView.name(), createView.columnNames(), function.apply(createView.query()));
    }

    @Override
    public AST visit(DecimalLiteral decimalLiteral) {
        return decimalLiteral;
    }

    @Override
    public AST visit(Distinct distinct) {
        return distinct(function.apply(distinct.input()));
    }

    @Override
    public AST visit(Divide divide) {
        return divide(function.apply(divide.left()), function.apply(divide.right()));
    }

    @Override
    public AST visit(DropTable dropTable) {
        return dropTable;
    }

    @Override
    public AST visit(Equal equal) {
        return equal(function.apply(equal.left()), function.apply(equal.right()));
    }

    @Override
    public AST visit(Exists exists) {
        return exists(function.apply(exists.input()));
    }

    @Override
    public AST visit(Explain explain) {
        return explain(function.apply(explain.input()));
    }

    @Override
    public AST visit(ExtractYear extractYear) {
        return extractYear(function.apply(extractYear.input()));
    }

    @Override
    public AST visit(Greater greater) {
        return greater(function.apply(greater.left()), function.apply(greater.right()));
    }

    @Override
    public AST visit(GreaterOrEqual greaterOrEqual) {
        return greaterOrEqual(function.apply(greaterOrEqual.left()), function.apply(greaterOrEqual.right()));
    }

    @Override
    public AST visit(GroupingSetsList groupingSetsList) {
        return groupingSetsList;
    }

    @Override
    public AST visit(In in) {
        return in(function.apply(in.input()), function.apply(in.value()));
    }

    @Override
    public AST visit(InnerJoin innerJoin) {
        return innerJoin(function.apply(innerJoin.left()),
                function.apply(innerJoin.right()),
                function.apply(innerJoin.joinSpecification()));
    }

    @Override
    public AST visit(InsertInto insertInto) {
        return insertInto(visit(insertInto.targetTable()), visit(insertInto.rows()));
    }

    @Override
    public AST visit(IntegerLiteral integerLiteral) {
        return integerLiteral;
    }

    @Override
    public AST visit(IntervalLiteral intervalLiteral) {
        return intervalLiteral;
    }

    @Override
    public AST visit(InValueList inValueList) {
        return inValueList(inValueList.values().stream().map(function).collect(Collectors.toList()));
    }

    @Override
    public AST visit(LeftJoin leftJoin) {
        return leftJoin(function.apply(leftJoin.left()),
                function.apply(leftJoin.right()),
                function.apply(leftJoin.joinSpecification()));
    }

    @Override
    public AST visit(Less less) {
        return less(function.apply(less.left()), function.apply(less.right()));
    }

    @Override
    public AST visit(LessOrEqual lessOrEqual) {
        return lessOrEqual(function.apply(lessOrEqual.left()), function.apply(lessOrEqual.right()));
    }

    @Override
    public AST visit(Like like) {
        return like(function.apply(like.input()), function.apply(like.pattern()));
    }

    @Override
    public AST visit(Limit limit) {
        return limit(function.apply(limit.input()), limit.limit());
    }

    @Override
    public AST visit(Max max) {
        return max(function.apply(max.input()));
    }

    @Override
    public AST visit(Min min) {
        return min(function.apply(min.input()));
    }

    @Override
    public AST visit(Minus minus) {
        return minus(function.apply(minus.left()), function.apply(minus.right()));
    }

    @Override
    public AST visit(Multiply multiply) {
        return multiply(function.apply(multiply.left()), function.apply(multiply.right()));
    }

    @Override
    public AST visit(Not not) {
        return not(function.apply(not.input()));
    }

    @Override
    public AST visit(NotEqual notEqual) {
        return notEqual(function.apply(notEqual.left()), function.apply(notEqual.right()));
    }

    @Override
    public AST visit(NullLiteral nullLiteral) {
        return nullLiteral;
    }

    @Override
    public AST visit(Or or) {
        return or(function.apply(or.left()), function.apply(or.left()));
    }

    @Override
    public AST visit(Placeholder placeholder) {
        return placeholder;
    }

    @Override
    public AST visit(Plus plus) {
        return plus(function.apply(plus.left()), function.apply(plus.right()));
    }

    @Override
    public AST visit(QualifiedColumnName qualifiedColumnReference) {
        return qualifiedColumnReference;
    }

    @Override
    public Row visit(Row row) {
        return row(row.values().stream().map(function).collect(Collectors.toList()));
    }

    @Override
    public AST visit(ScalarSubquery scalarSubquery) {
        return scalarSubquery(function.apply(scalarSubquery.input()));
    }

    @Override
    public AST visit(Sum sum) {
        return sum(function.apply(sum.input()));
    }

    @Override
    public AST visit(Select select) {
        return select(select.outputColumns().stream().map(function).collect(Collectors.toList()),
                function.apply(select.fromClause()),
                Optional.ofNullable(select.whereClause()).map(function).orElse(null),
                select.groupByClause(),
                Optional.ofNullable(select.havingClause()).map(function).orElse(null),
                select.orderByClause());
    }

    @Override
    public AST visit(SemiJoin semiJoin) {
        return semiJoin(function.apply(semiJoin.left()),
                function.apply(semiJoin.right()),
                function.apply(semiJoin.predicate()));
    }

    @Override
    public AST visit(SortSpecification sortSpecification) {
        return sortSpecification;
    }

    @Override
    public AST visit(SortSpecificationList sortSpecificationList) {
        return sortSpecificationList;
    }

    @Override
    public AST visit(Substring substring) {
        return substring(function.apply(substring.input()),
                function.apply(substring.start()),
                function.apply(substring.length()));
    }

    @Override
    public AST visit(TableAlias tableAlias) {
        return tableAlias(function.apply(tableAlias.input()), tableAlias.alias());
    }

    @Override
    public AST visit(TableAliasWithColumns tableAliasWithColumns) {
        return tableAliasWithColumns(function.apply(tableAliasWithColumns.input()),
                tableAliasWithColumns.tableAlias(),
                tableAliasWithColumns.columnAliases());
    }

    @Override
    public AST visit(TableElementList tableElementList) {
        return tableElementList;
    }

    @Override
    public TableName visit(TableName tableName) {
        return tableName;
    }

    @Override
    public AST visit(TableReferenceList tableReferenceList) {
        return tableReferenceList(function.apply(tableReferenceList.first()),
                function.apply(tableReferenceList.second()),
                tableReferenceList.others().stream().map(function).collect(Collectors.toList()));
    }

    @Override
    public AST visit(TextLiteral textLiteral) {
        return textLiteral;
    }

    @Override
    public AST visit(UnqualifiedColumnName unqualifiedColumnReference) {
        return unqualifiedColumnReference;
    }

    @Override
    public Values visit(Values values) {
        return rows(values.rows().stream().map(this::visit).collect(Collectors.toList()));
    }

    @Override
    public AST visit(IsNull isNull) {
        return isNull(function.apply(isNull.input()));
    }

    @Override
    public AST visit(With with) {
        return with(visit(with.withList()), function.apply(with.query()));
    }

    @Override
    public WithElement visit(WithElement withElement) {
        return withElement(withElement.name(), function.apply(withElement.definition()));
    }

    @Override
    public WithList visit(WithList withList) {
        return withList(withList.elements().stream().map(this::visit).collect(Collectors.toList()));
    }
}

package fr.awildelephant.rdbms.ast.visitor;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Asterisk;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.ColumnAlias;
import fr.awildelephant.rdbms.ast.ColumnDefinition;
import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.CreateView;
import fr.awildelephant.rdbms.ast.Distinct;
import fr.awildelephant.rdbms.ast.DropTable;
import fr.awildelephant.rdbms.ast.Exists;
import fr.awildelephant.rdbms.ast.Explain;
import fr.awildelephant.rdbms.ast.GroupingSetsList;
import fr.awildelephant.rdbms.ast.InValueList;
import fr.awildelephant.rdbms.ast.InnerJoin;
import fr.awildelephant.rdbms.ast.InsertInto;
import fr.awildelephant.rdbms.ast.LeftJoin;
import fr.awildelephant.rdbms.ast.Limit;
import fr.awildelephant.rdbms.ast.QualifiedColumnName;
import fr.awildelephant.rdbms.ast.Row;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.SemiJoin;
import fr.awildelephant.rdbms.ast.SortSpecification;
import fr.awildelephant.rdbms.ast.SortSpecificationList;
import fr.awildelephant.rdbms.ast.Substring;
import fr.awildelephant.rdbms.ast.TableAlias;
import fr.awildelephant.rdbms.ast.TableAliasWithColumns;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.ast.TableReferenceList;
import fr.awildelephant.rdbms.ast.UnqualifiedColumnName;
import fr.awildelephant.rdbms.ast.Values;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.WithElement;
import fr.awildelephant.rdbms.ast.WithList;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Any;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CaseWhen;
import fr.awildelephant.rdbms.ast.value.Count;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.ExtractYear;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.GreaterOrEqual;
import fr.awildelephant.rdbms.ast.value.In;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.IsNull;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Max;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NotEqual;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Placeholder;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.ScalarSubquery;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

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
        return and(function.apply(and.leftChild()), function.apply(and.rightChild()));
    }

    @Override
    public AST visit(Any any) {
        return any(function.apply(any.child()));
    }

    @Override
    public AST visit(Asterisk asterisk) {
        return asterisk;
    }

    @Override
    public AST visit(Avg avg) {
        return avg(function.apply(avg.child()));
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
        return cast(function.apply(cast.child()), cast.targetType());
    }

    @Override
    public AST visit(ColumnAlias columnAlias) {
        return columnAlias(function.apply(columnAlias.child()), columnAlias.alias());
    }

    @Override
    public AST visit(ColumnDefinition columnDefinition) {
        return columnDefinition;
    }

    @Override
    public AST visit(Count count) {
        return count(count.distinct(), function.apply(count.child()));
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
        return distinct(function.apply(distinct.child()));
    }

    @Override
    public AST visit(Divide divide) {
        return divide(function.apply(divide.leftChild()), function.apply(divide.rightChild()));
    }

    @Override
    public AST visit(DropTable dropTable) {
        return dropTable;
    }

    @Override
    public AST visit(Equal equal) {
        return equal(function.apply(equal.leftChild()), function.apply(equal.rightChild()));
    }

    @Override
    public AST visit(Exists exists) {
        return exists(function.apply(exists.child()));
    }

    @Override
    public AST visit(Explain explain) {
        return explain(function.apply(explain.child()));
    }

    @Override
    public AST visit(ExtractYear extractYear) {
        return extractYear(function.apply(extractYear.child()));
    }

    @Override
    public AST visit(Greater greater) {
        return greater(function.apply(greater.leftChild()), function.apply(greater.rightChild()));
    }

    @Override
    public AST visit(GreaterOrEqual greaterOrEqual) {
        return greaterOrEqual(function.apply(greaterOrEqual.leftChild()), function.apply(greaterOrEqual.rightChild()));
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
        return less(function.apply(less.leftChild()), function.apply(less.rightChild()));
    }

    @Override
    public AST visit(LessOrEqual lessOrEqual) {
        return lessOrEqual(function.apply(lessOrEqual.leftChild()), function.apply(lessOrEqual.rightChild()));
    }

    @Override
    public AST visit(Like like) {
        return like(function.apply(like.input()), function.apply(like.pattern()));
    }

    @Override
    public AST visit(Limit limit) {
        return limit(function.apply(limit.child()), limit.limit());
    }

    @Override
    public AST visit(Max max) {
        return max(function.apply(max.child()));
    }

    @Override
    public AST visit(Min min) {
        return min(function.apply(min.child()));
    }

    @Override
    public AST visit(Minus minus) {
        return minus(function.apply(minus.leftChild()), function.apply(minus.rightChild()));
    }

    @Override
    public AST visit(Multiply multiply) {
        return multiply(function.apply(multiply.leftChild()), function.apply(multiply.rightChild()));
    }

    @Override
    public AST visit(Not not) {
        return not(function.apply(not.child()));
    }

    @Override
    public AST visit(NotEqual notEqual) {
        return notEqual(function.apply(notEqual.leftChild()), function.apply(notEqual.rightChild()));
    }

    @Override
    public AST visit(NullLiteral nullLiteral) {
        return nullLiteral;
    }

    @Override
    public AST visit(Or or) {
        return or(function.apply(or.leftChild()), function.apply(or.leftChild()));
    }

    @Override
    public AST visit(Placeholder placeholder) {
        return placeholder;
    }

    @Override
    public AST visit(Plus plus) {
        return plus(function.apply(plus.leftChild()), function.apply(plus.rightChild()));
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
        return scalarSubquery(function.apply(scalarSubquery.child()));
    }

    @Override
    public AST visit(Sum sum) {
        return sum(function.apply(sum.child()));
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
        return tableAlias(function.apply(tableAlias.child()), tableAlias.alias());
    }

    @Override
    public AST visit(TableAliasWithColumns tableAliasWithColumns) {
        return tableAliasWithColumns(function.apply(tableAliasWithColumns.child()),
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

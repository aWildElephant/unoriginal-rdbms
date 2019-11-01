package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.GreaterOrEqual;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Min;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Placeholder;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.ScalarSubquery;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

public abstract class DefaultASTVisitor<T> implements ASTVisitor<T> {

    @Override
    public T visit(And and) {
        return defaultVisit(and);
    }

    @Override
    public T visit(Asterisk asterisk) {
        return defaultVisit(asterisk);
    }

    @Override
    public T visit(Avg avg) {
        return defaultVisit(avg);
    }

    @Override
    public T visit(Between between) {
        return defaultVisit(between);
    }

    @Override
    public T visit(BooleanLiteral booleanLiteral) {
        return defaultVisit(booleanLiteral);
    }

    @Override
    public T visit(Cast cast) {
        return defaultVisit(cast);
    }

    @Override
    public T visit(ColumnAlias columnAlias) {
        return defaultVisit(columnAlias);
    }

    @Override
    public T visit(ColumnDefinition columnDefinition) {
        return defaultVisit(columnDefinition);
    }

    @Override
    public T visit(CountStar countStar) {
        return defaultVisit(countStar);
    }

    @Override
    public T visit(CreateTable createTable) {
        return defaultVisit(createTable);
    }

    @Override
    public T visit(DecimalLiteral decimalLiteral) {
        return defaultVisit(decimalLiteral);
    }

    @Override
    public T visit(Distinct distinct) {
        return defaultVisit(distinct);
    }

    @Override
    public T visit(Divide divide) {
        return defaultVisit(divide);
    }

    @Override
    public T visit(DropTable dropTable) {
        return defaultVisit(dropTable);
    }

    @Override
    public T visit(Equal equal) {
        return defaultVisit(equal);
    }

    @Override
    public T visit(Greater greater) {
        return defaultVisit(greater);
    }

    @Override
    public T visit(GreaterOrEqual greaterOrEqual) {
        return defaultVisit(greaterOrEqual);
    }

    @Override
    public T visit(GroupBy groupBy) {
        return defaultVisit(groupBy);
    }

    @Override
    public T visit(GroupingSetsList groupingSetsList) {
        return defaultVisit(groupingSetsList);
    }

    @Override
    public T visit(InnerJoin innerJoin) {
        return defaultVisit(innerJoin);
    }

    @Override
    public T visit(InsertInto insertInto) {
        return defaultVisit(insertInto);
    }

    @Override
    public T visit(IntegerLiteral integerLiteral) {
        return defaultVisit(integerLiteral);
    }

    @Override
    public T visit(IntervalLiteral intervalLiteral) {
        return defaultVisit(intervalLiteral);
    }

    @Override
    public T visit(Less less) {
        return defaultVisit(less);
    }

    @Override
    public T visit(LessOrEqual lessOrEqual) {
        return defaultVisit(lessOrEqual);
    }

    @Override
    public T visit(Like like) {
        return defaultVisit(like);
    }

    @Override
    public T visit(Limit limit) {
        return defaultVisit(limit);
    }

    @Override
    public T visit(Min min) {
        return defaultVisit(min);
    }

    @Override
    public T visit(Minus minus) {
        return defaultVisit(minus);
    }

    @Override
    public T visit(Multiply multiply) {
        return defaultVisit(multiply);
    }

    @Override
    public T visit(Not not) {
        return defaultVisit(not);
    }

    @Override
    public T visit(NullLiteral nullLiteral) {
        return defaultVisit(nullLiteral);
    }

    @Override
    public T visit(Or or) {
        return defaultVisit(or);
    }

    @Override
    public T visit(Placeholder placeholder) {
        return defaultVisit(placeholder);
    }

    @Override
    public T visit(Plus plus) {
        return defaultVisit(plus);
    }

    @Override
    public T visit(QualifiedColumnName qualifiedColumnReference) {
        return defaultVisit(qualifiedColumnReference);
    }

    @Override
    public T visit(Row row) {
        return defaultVisit(row);
    }

    @Override
    public T visit(ScalarSubquery scalarSubquery) {
        return defaultVisit(scalarSubquery);
    }

    @Override
    public T visit(Values values) {
        return defaultVisit(values);
    }

    @Override
    public T visit(SortedSelect sortedSelect) {
        return defaultVisit(sortedSelect);
    }

    @Override
    public T visit(SortSpecification sortSpecification) {
        return defaultVisit(sortSpecification);
    }

    @Override
    public T visit(SortSpecificationList sortSpecificationList) {
        return defaultVisit(sortSpecificationList);
    }

    @Override
    public T visit(Sum sum) {
        return defaultVisit(sum);
    }

    @Override
    public T visit(TableAlias tableAlias) {
        return defaultVisit(tableAlias);
    }

    @Override
    public T visit(TableElementList tableElementList) {
        return defaultVisit(tableElementList);
    }

    @Override
    public T visit(TableName tableName) {
        return defaultVisit(tableName);
    }

    @Override
    public T visit(TableReferenceList tableReferenceList) {
        return defaultVisit(tableReferenceList);
    }

    @Override
    public T visit(TextLiteral textLiteral) {
        return defaultVisit(textLiteral);
    }

    @Override
    public T visit(UnqualifiedColumnName unqualifiedColumnReference) {
        return defaultVisit(unqualifiedColumnReference);
    }

    @Override
    public T visit(Where where) {
        return defaultVisit(where);
    }

    public abstract T defaultVisit(AST node);
}

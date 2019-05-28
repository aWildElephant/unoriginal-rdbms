package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.IntervalLiteral;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Plus;
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
    public T visit(ColumnName columnName) {
        return defaultVisit(columnName);
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
    public T visit(GroupBy groupBy) {
        return defaultVisit(groupBy);
    }

    @Override
    public T visit(GroupingSetsList groupingSetsList) {
        return defaultVisit(groupingSetsList);
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
    public T visit(LessOrEqual lessOrEqual) {
        return defaultVisit(lessOrEqual);
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
    public T visit(Plus plus) {
        return defaultVisit(plus);
    }

    @Override
    public T visit(Row row) {
        return defaultVisit(row);
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
    public T visit(SortSpecificationList sortSpecificationList) {
        return defaultVisit(sortSpecificationList);
    }

    @Override
    public T visit(Sum sum) {
        return defaultVisit(sum);
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
    public T visit(TextLiteral textLiteral) {
        return defaultVisit(textLiteral);
    }

    @Override
    public T visit(Where where) {
        return defaultVisit(where);
    }

    public abstract T defaultVisit(AST node);
}
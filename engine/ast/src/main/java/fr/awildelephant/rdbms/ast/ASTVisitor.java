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
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

import java.util.function.Function;

public interface ASTVisitor<T> extends Function<AST, T> {

    default T apply(AST node) {
        return node.accept(this);
    }

    T visit(And and);

    T visit(Asterisk asterisk);

    T visit(Avg avg);

    T visit(BooleanLiteral booleanLiteral);

    T visit(Cast cast);

    T visit(ColumnAlias columnAlias);

    T visit(ColumnDefinition columnDefinition);

    T visit(ColumnName columnName);

    T visit(CountStar countStar);

    T visit(CreateTable createTable);

    T visit(DecimalLiteral decimalLiteral);

    T visit(Distinct distinct);

    T visit(Divide divide);

    T visit(DropTable dropTable);

    T visit(Equal equal);

    T visit(GroupBy groupBy);

    T visit(GroupingSetsList groupingSetsList);

    T visit(InsertInto insertInto);

    T visit(IntegerLiteral integerLiteral);

    T visit(IntervalLiteral intervalLiteral);

    T visit(Less less);

    T visit(LessOrEqual lessOrEqual);

    T visit(Minus minus);

    T visit(Multiply multiply);

    T visit(Not not);

    T visit(NullLiteral nullLiteral);

    T visit(Or or);

    T visit(Plus plus);

    T visit(Row row);

    T visit(Values values);

    T visit(Sum sum);

    T visit(SortedSelect sortedSelect);

    T visit(SortSpecificationList sortSpecificationList);

    T visit(TableElementList tableElementList);

    T visit(TableName tableName);

    T visit(TableReferenceList tableReferenceList);

    T visit(TextLiteral textLiteral);

    T visit(Where where);
}

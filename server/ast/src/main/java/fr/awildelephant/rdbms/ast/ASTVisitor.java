package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.Sum;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

import java.util.function.Function;

public interface ASTVisitor<T> extends Function<AST, T> {

    default T apply(AST node) {
        return node.accept(this);
    }

    T visit(Asterisk asterisk);

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

    T visit(GroupBy groupBy);

    T visit(GroupingSetsList groupingSetsList);

    T visit(InsertInto insertInto);

    T visit(IntegerLiteral integerLiteral);

    T visit(Minus minus);

    T visit(Multiply multiply);

    T visit(NullLiteral nullLiteral);

    T visit(Plus plus);

    T visit(Row row);

    T visit(Rows rows);

    T visit(Sum sum);

    T visit(Select select);

    T visit(TableElementList tableElementList);

    T visit(TableName tableName);

    T visit(TextLiteral textLiteral);
}

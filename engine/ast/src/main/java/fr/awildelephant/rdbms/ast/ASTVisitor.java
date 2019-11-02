package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Avg;
import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
import fr.awildelephant.rdbms.ast.value.CaseWhen;
import fr.awildelephant.rdbms.ast.value.CountStar;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.ExtractYear;
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

import java.util.function.Function;

public interface ASTVisitor<T> extends Function<AST, T> {

    default T apply(AST node) {
        return node.accept(this);
    }

    T visit(And and);

    T visit(Asterisk asterisk);

    T visit(Avg avg);

    T visit(Between between);

    T visit(BooleanLiteral booleanLiteral);

    T visit(CaseWhen caseWhen);

    T visit(Cast cast);

    T visit(ColumnAlias columnAlias);

    T visit(ColumnDefinition columnDefinition);

    T visit(CountStar countStar);

    T visit(CreateTable createTable);

    T visit(DecimalLiteral decimalLiteral);

    T visit(Distinct distinct);

    T visit(Divide divide);

    T visit(DropTable dropTable);

    T visit(Equal equal);

    T visit(ExtractYear extractYear);

    T visit(Greater greater);

    T visit(GreaterOrEqual greaterOrEqual);

    T visit(GroupBy groupBy);

    T visit(GroupingSetsList groupingSetsList);

    T visit(InnerJoin innerJoin);

    T visit(InsertInto insertInto);

    T visit(IntegerLiteral integerLiteral);

    T visit(IntervalLiteral intervalLiteral);

    T visit(Less less);

    T visit(LessOrEqual lessOrEqual);

    T visit(Like like);

    T visit(Limit limit);

    T visit(Min min);

    T visit(Minus minus);

    T visit(Multiply multiply);

    T visit(Not not);

    T visit(NullLiteral nullLiteral);

    T visit(Or or);

    T visit(Placeholder placeholder);

    T visit(Plus plus);

    T visit(QualifiedColumnName qualifiedColumnReference);

    T visit(Row row);

    T visit(ScalarSubquery scalarSubquery);

    T visit(Sum sum);

    T visit(SortedSelect sortedSelect);

    T visit(SortSpecification sortSpecification);

    T visit(SortSpecificationList sortSpecificationList);

    T visit(TableAlias tableAlias);

    T visit(TableElementList tableElementList);

    T visit(TableName tableName);

    T visit(TableReferenceList tableReferenceList);

    T visit(TextLiteral textLiteral);

    T visit(UnqualifiedColumnName unqualifiedColumnReference);

    T visit(Values values);

    T visit(Where where);
}

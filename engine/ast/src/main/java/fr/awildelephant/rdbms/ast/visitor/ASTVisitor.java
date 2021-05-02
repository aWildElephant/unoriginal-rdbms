package fr.awildelephant.rdbms.ast.visitor;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.AntiJoin;
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
import fr.awildelephant.rdbms.ast.value.*;

import java.util.function.Function;

public interface ASTVisitor<T> extends Function<AST, T> {

    default T apply(AST node) {
        return node.accept(this);
    }

    T visit(And and);

    T visit(AntiJoin antiJoin);

    T visit(Any any);

    T visit(Asterisk asterisk);

    T visit(Avg avg);

    T visit(Between between);

    T visit(BooleanLiteral booleanLiteral);

    T visit(CaseWhen caseWhen);

    T visit(Cast cast);

    T visit(ColumnAlias columnAlias);

    T visit(ColumnDefinition columnDefinition);

    T visit(Count count);

    T visit(CountStar countStar);

    T visit(CreateTable createTable);

    T visit(CreateView createView);

    T visit(DecimalLiteral decimalLiteral);

    T visit(Distinct distinct);

    T visit(Divide divide);

    T visit(DropTable dropTable);

    T visit(Equal equal);

    T visit(Exists exists);

    T visit(Explain explain);

    T visit(ExtractYear extractYear);

    T visit(Greater greater);

    T visit(GreaterOrEqual greaterOrEqual);

    T visit(GroupingSetsList groupingSetsList);

    T visit(In in);

    T visit(InnerJoin innerJoin);

    T visit(InsertInto insertInto);

    T visit(IntegerLiteral integerLiteral);

    T visit(IntervalLiteral intervalLiteral);

    T visit(InValueList inValueList);

    T visit(LeftJoin leftJoin);

    T visit(Less less);

    T visit(LessOrEqual lessOrEqual);

    T visit(Like like);

    T visit(Limit limit);

    T visit(Max max);

    T visit(Min min);

    T visit(Minus minus);

    T visit(Multiply multiply);

    T visit(Not not);

    T visit(NotEqual notEqual);

    T visit(NullLiteral nullLiteral);

    T visit(Or or);

    T visit(Placeholder placeholder);

    T visit(Plus plus);

    T visit(QualifiedColumnName qualifiedColumnReference);

    T visit(Row row);

    T visit(ScalarSubquery scalarSubquery);

    T visit(Sum sum);

    T visit(Select select);

    T visit(SemiJoin semiJoin);

    T visit(SortSpecification sortSpecification);

    T visit(SortSpecificationList sortSpecificationList);

    T visit(Substring substring);

    T visit(TableAlias tableAlias);

    T visit(TableAliasWithColumns tableAliasWithColumns);

    T visit(TableElementList tableElementList);

    T visit(TableName tableName);

    T visit(TableReferenceList tableReferenceList);

    T visit(TextLiteral textLiteral);

    T visit(UnqualifiedColumnName unqualifiedColumnReference);

    T visit(Values values);

    T visit(IsNull isNull);

    T visit(With with);

    T visit(WithElement withElement);

    T visit(WithList withList);
}

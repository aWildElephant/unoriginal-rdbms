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
import fr.awildelephant.rdbms.ast.ReadCSV;
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
import fr.awildelephant.rdbms.ast.Truncate;
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

public abstract class DefaultASTVisitor<T> implements ASTVisitor<T> {

    @Override
    public T visit(And and) {
        return defaultVisit(and);
    }

    @Override
    public T visit(Any any) {
        return defaultVisit(any);
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
    public T visit(CaseWhen caseWhen) {
        return defaultVisit(caseWhen);
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
    public T visit(Count count) {
        return defaultVisit(count);
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
    public T visit(CreateView createView) {
        return defaultVisit(createView);
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
    public T visit(Exists exists) {
        return defaultVisit(exists);
    }

    @Override
    public T visit(Explain explain) {
        return defaultVisit(explain);
    }

    @Override
    public T visit(ExtractYear extractYear) {
        return defaultVisit(extractYear);
    }

    @Override
    public T visit(ReadCSV ReadCSV) {
        return defaultVisit(ReadCSV);
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
    public T visit(GroupingSetsList groupingSetsList) {
        return defaultVisit(groupingSetsList);
    }

    @Override
    public T visit(In in) {
        return defaultVisit(in);
    }

    @Override
    public T visit(InValueList inValueList) {
        return defaultVisit(inValueList);
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
    public T visit(IsNull isNull) {
        return defaultVisit(isNull);
    }

    @Override
    public T visit(LeftJoin leftJoin) {
        return defaultVisit(leftJoin);
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
    public T visit(Max max) {
        return defaultVisit(max);
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
    public T visit(NotEqual notEqual) {
        return defaultVisit(notEqual);
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
    public T visit(Substring substring) {
        return defaultVisit(substring);
    }

    @Override
    public T visit(Values values) {
        return defaultVisit(values);
    }

    @Override
    public T visit(Select select) {
        return defaultVisit(select);
    }

    @Override
    public T visit(SemiJoin semiJoin) {
        return defaultVisit(semiJoin);
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
    public T visit(TableAliasWithColumns tableAliasWithColumns) {
        return defaultVisit(tableAliasWithColumns);
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
    public T visit(Truncate truncate) {
        return defaultVisit(truncate);
    }

    @Override
    public T visit(UnqualifiedColumnName unqualifiedColumnReference) {
        return defaultVisit(unqualifiedColumnReference);
    }

    @Override
    public T visit(With with) {
        return defaultVisit(with);
    }

    @Override
    public T visit(WithElement withElement) {
        return defaultVisit(withElement);
    }

    @Override
    public T visit(WithList withList) {
        return defaultVisit(withList);
    }

    public abstract T defaultVisit(AST node);
}

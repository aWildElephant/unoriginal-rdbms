package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.Exists;
import fr.awildelephant.rdbms.ast.InValueList;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.In;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NotEqual;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Plus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.ColumnAlias.columnAlias;
import static fr.awildelephant.rdbms.ast.InValueList.inValueList;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.ast.UnqualifiedColumnName.unqualifiedColumnName;
import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.CountStar.countStar;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.In.in;
import static fr.awildelephant.rdbms.ast.value.IntegerLiteral.integerLiteral;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;
import static fr.awildelephant.rdbms.ast.value.Like.like;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.NotEqual.notEqual;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;
import static fr.awildelephant.rdbms.ast.value.ScalarSubquery.scalarSubquery;

public final class SubqueryExtractor extends DefaultASTVisitor<AST> {

    private final List<AST> subqueries = new ArrayList<>();

    List<AST> subqueries() {
        return subqueries;
    }

    @Override
    public AST visit(And and) {
        return and(apply(and.left()), apply(and.right()));
    }

    @Override
    public AST visit(Cast cast) {
        return cast(apply(cast.input()), cast.targetType());
    }

    @Override
    public AST visit(Divide divide) {
        return divide(apply(divide.left()), apply(divide.right()));
    }

    @Override
    public AST visit(Equal equal) {
        return equal(apply(equal.left()), apply(equal.right()));
    }

    @Override
    public AST visit(Exists exists) {
        final String id = UUID.randomUUID().toString();

        final Select subquery = select(List.of(columnAlias(countStar(), id)),
                                       exists.input(),
                                       null,
                                       null,
                                       null,
                                       null);

        subqueries.add(subquery);

        return greater(unqualifiedColumnName(id), integerLiteral(0));
    }

    @Override
    public AST visit(Greater greater) {
        return greater(apply(greater.left()), apply(greater.right()));
    }

    @Override
    public AST visit(In in) {
        return in(apply(in.input()), apply(in.value()));
    }

    @Override
    public AST visit(InValueList inValueList) {
        final List<AST> values = new ArrayList<>();
        for (AST value : inValueList.values()) {
            values.add(apply(value));
        }

        return inValueList(values);
    }

    @Override
    public AST visit(Less less) {
        return less(apply(less.left()), apply(less.right()));
    }

    @Override
    public AST visit(LessOrEqual lessOrEqual) {
        return lessOrEqual(apply(lessOrEqual.left()), apply(lessOrEqual.right()));
    }

    @Override
    public AST visit(Like like) {
        return like(apply(like.input()), apply(like.pattern()));
    }

    @Override
    public AST visit(Multiply multiply) {
        return multiply(apply(multiply.left()), apply(multiply.right()));
    }

    @Override
    public AST visit(Not not) {
        return not(apply(not.input()));
    }

    @Override
    public AST visit(NotEqual notEqual) {
        return notEqual(apply(notEqual.left()), apply(notEqual.right()));
    }

    @Override
    public AST visit(Or or) {
        return or(apply(or.left()), apply(or.right()));
    }

    @Override
    public AST visit(Plus plus) {
        return plus(apply(plus.left()), apply(plus.right()));
    }

    @Override
    public AST visit(Minus minus) {
        return minus(apply(minus.left()), apply(minus.right()));
    }

    @Override
    public AST visit(Select select) {
        final String id = UUID.randomUUID().toString();

        subqueries.add(scalarSubquery(select, id));

        return unqualifiedColumnName(id);
    }

    @Override
    public AST defaultVisit(AST node) {
        return node;
    }
}

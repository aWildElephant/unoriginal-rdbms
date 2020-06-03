package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.*;

import static fr.awildelephant.rdbms.ast.Cast.cast;
import static fr.awildelephant.rdbms.ast.value.And.and;
import static fr.awildelephant.rdbms.ast.value.Divide.divide;
import static fr.awildelephant.rdbms.ast.value.Equal.equal;
import static fr.awildelephant.rdbms.ast.value.Greater.greater;
import static fr.awildelephant.rdbms.ast.value.Less.less;
import static fr.awildelephant.rdbms.ast.value.LessOrEqual.lessOrEqual;
import static fr.awildelephant.rdbms.ast.value.Like.like;
import static fr.awildelephant.rdbms.ast.value.Minus.minus;
import static fr.awildelephant.rdbms.ast.value.Multiply.multiply;
import static fr.awildelephant.rdbms.ast.value.Not.not;
import static fr.awildelephant.rdbms.ast.value.NotEqual.notEqual;
import static fr.awildelephant.rdbms.ast.value.Or.or;
import static fr.awildelephant.rdbms.ast.value.Plus.plus;

// TODO: gérer les constantes ici
public abstract class DefaultFormulaRewriter extends DefaultASTVisitor<AST> {

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
    public AST visit(Greater greater) {
        return greater(apply(greater.left()), apply(greater.right()));
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
}

package fr.awildelephant.rdbms.algebraizer.formula;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Divide;
import fr.awildelephant.rdbms.ast.value.Equal;
import fr.awildelephant.rdbms.ast.value.Greater;
import fr.awildelephant.rdbms.ast.value.Less;
import fr.awildelephant.rdbms.ast.value.LessOrEqual;
import fr.awildelephant.rdbms.ast.value.Like;
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NotEqual;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.visitor.DefaultASTVisitor;

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

public abstract class DefaultFormulaRewriter extends DefaultASTVisitor<AST> {

    @Override
    public AST visit(And and) {
        return and(apply(and.leftChild()), apply(and.rightChild()));
    }

    @Override
    public AST visit(Cast cast) {
        return cast(apply(cast.child()), cast.targetType());
    }

    @Override
    public AST visit(Divide divide) {
        return divide(apply(divide.leftChild()), apply(divide.rightChild()));
    }

    @Override
    public AST visit(Equal equal) {
        return equal(apply(equal.leftChild()), apply(equal.rightChild()));
    }

    @Override
    public AST visit(Greater greater) {
        return greater(apply(greater.leftChild()), apply(greater.rightChild()));
    }


    @Override
    public AST visit(Less less) {
        return less(apply(less.leftChild()), apply(less.rightChild()));
    }

    @Override
    public AST visit(LessOrEqual lessOrEqual) {
        return lessOrEqual(apply(lessOrEqual.leftChild()), apply(lessOrEqual.rightChild()));
    }

    @Override
    public AST visit(Like like) {
        return like(apply(like.input()), apply(like.pattern()));
    }

    @Override
    public AST visit(Multiply multiply) {
        return multiply(apply(multiply.leftChild()), apply(multiply.rightChild()));
    }

    @Override
    public AST visit(Not not) {
        return not(apply(not.child()));
    }

    @Override
    public AST visit(NotEqual notEqual) {
        return notEqual(apply(notEqual.leftChild()), apply(notEqual.rightChild()));
    }

    @Override
    public AST visit(Or or) {
        return or(apply(or.leftChild()), apply(or.rightChild()));
    }

    @Override
    public AST visit(Plus plus) {
        return plus(apply(plus.leftChild()), apply(plus.rightChild()));
    }

    @Override
    public AST visit(Minus minus) {
        return minus(apply(minus.leftChild()), apply(minus.rightChild()));
    }
}

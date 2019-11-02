package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Cast;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.And;
import fr.awildelephant.rdbms.ast.value.Between;
import fr.awildelephant.rdbms.ast.value.BooleanLiteral;
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
import fr.awildelephant.rdbms.ast.value.Minus;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.Not;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
import fr.awildelephant.rdbms.ast.value.Or;
import fr.awildelephant.rdbms.ast.value.Placeholder;
import fr.awildelephant.rdbms.ast.value.Plus;
import fr.awildelephant.rdbms.ast.value.TextLiteral;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Differentiates between a formula and not a formula
 * <p>
 * TODO: an effort
 */
public final class FormulaOrNotFormulaDifferentiator extends DefaultASTVisitor<Boolean> {

    private static final FormulaOrNotFormulaDifferentiator INSTANCE = new FormulaOrNotFormulaDifferentiator();

    static boolean isFormula(AST ast) {
        return INSTANCE.apply(ast);
    }

    @Override
    public Boolean visit(And and) {
        return TRUE;
    }

    @Override
    public Boolean visit(Between between) {
        return TRUE;
    }

    @Override
    public Boolean visit(BooleanLiteral booleanLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(Cast cast) {
        return TRUE;
    }

    @Override
    public Boolean visit(DecimalLiteral decimalLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(Divide divide) {
        return TRUE;
    }

    @Override
    public Boolean visit(Equal equal) {
        return TRUE;
    }

    @Override
    public Boolean visit(ExtractYear extractYear) {
        return TRUE;
    }

    @Override
    public Boolean visit(Greater greater) {
        return TRUE;
    }

    @Override
    public Boolean visit(GreaterOrEqual greaterOrEqual) {
        return TRUE;
    }

    @Override
    public Boolean visit(IntegerLiteral integerLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(IntervalLiteral interval) {
        return TRUE;
    }

    @Override
    public Boolean visit(Less less) {
        return TRUE;
    }

    @Override
    public Boolean visit(LessOrEqual lessOrEqual) {
        return TRUE;
    }

    @Override
    public Boolean visit(Like like) {
        return TRUE;
    }

    @Override
    public Boolean visit(Multiply multiply) {
        return TRUE;
    }

    @Override
    public Boolean visit(Not not) {
        return TRUE;
    }

    @Override
    public Boolean visit(NullLiteral nullLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(Or or) {
        return TRUE;
    }

    @Override
    public Boolean visit(Placeholder placeholder) {
        return TRUE;
    }

    @Override
    public Boolean visit(Plus plus) {
        return TRUE;
    }

    @Override
    public Boolean visit(Minus minus) {
        return TRUE;
    }

    @Override
    public Boolean visit(TextLiteral textLiteral) {
        return TRUE;
    }

    @Override
    public Boolean defaultVisit(AST node) {
        return FALSE;
    }
}

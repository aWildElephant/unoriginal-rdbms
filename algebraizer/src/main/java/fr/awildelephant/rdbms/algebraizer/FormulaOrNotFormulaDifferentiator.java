package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.value.DecimalLiteral;
import fr.awildelephant.rdbms.ast.value.IntegerLiteral;
import fr.awildelephant.rdbms.ast.value.Multiply;
import fr.awildelephant.rdbms.ast.value.NullLiteral;
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
    public Boolean visit(DecimalLiteral decimalLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(IntegerLiteral integerLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(Multiply multiply) {
        return TRUE;
    }

    @Override
    public Boolean visit(NullLiteral nullLiteral) {
        return TRUE;
    }

    @Override
    public Boolean visit(Plus plus) {
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

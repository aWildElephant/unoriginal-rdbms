package fr.awildelephant.rdbms.evaluator.operation.bool;

import fr.awildelephant.rdbms.evaluator.operation.BinaryOperation;
import fr.awildelephant.rdbms.evaluator.operation.text.TextOperation;
import fr.awildelephant.rdbms.util.logic.ThreeValuedLogic;

import java.util.regex.Pattern;

import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.FALSE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.TRUE;
import static fr.awildelephant.rdbms.util.logic.ThreeValuedLogic.UNKNOWN;

public final class LikePredicate extends BinaryOperation<TextOperation, TextOperation> implements BooleanOperation {

    private LikePredicate(TextOperation input, TextOperation patternOperation) {
        super(input, patternOperation);
    }

    public static LikePredicate likePredicate(TextOperation input, TextOperation patternOperation) {
        return new LikePredicate(input, patternOperation);
    }

    private Pattern createJavaPattern(String patternString) {
        // TODO: il faut échapper les caractères spéciaux de regexp java dans la regexp SQL
        return Pattern.compile(patternString.replace("%", ".*"));
    }

    // TODO: les deux inputs devraient évaluer comme du texte, pas besoin de wrapper
    @Override
    public ThreeValuedLogic evaluateBoolean() {
        final String value = firstChild().evaluateString();

        if (value == null) {
            return UNKNOWN;
        }

        // TODO: si l'opération est constante, on peut compiler le pattern une seule fois
        final String pattern = secondChild().evaluateString();

        if (pattern == null) {
            return UNKNOWN;
        }

        return createJavaPattern(pattern).matcher(value).matches() ? TRUE : FALSE;
    }
}

package fr.awildelephant.rdbms.evaluator.operation;

import fr.awildelephant.rdbms.data.value.DomainValue;
import fr.awildelephant.rdbms.schema.Domain;

import java.util.regex.Pattern;

import static fr.awildelephant.rdbms.data.value.FalseValue.falseValue;
import static fr.awildelephant.rdbms.data.value.NullValue.nullValue;
import static fr.awildelephant.rdbms.data.value.TrueValue.trueValue;
import static fr.awildelephant.rdbms.schema.Domain.BOOLEAN;

public final class LikePredicate implements Operation {

    private final Operation input;
    private final Operation patternOperation;

    private LikePredicate(Operation input, Operation patternOperation) {
        this.input = input;
        this.patternOperation = patternOperation;
    }

    public static LikePredicate likePredicate(Operation input, Operation patternOperation) {
        return new LikePredicate(input, patternOperation);
    }

    @Override
    public DomainValue evaluate() {
        final DomainValue value = input.evaluate();

        if (value.isNull()) {
            return nullValue();
        }

        // TODO: si l'opération est constante, on peut compiler le pattern une seule fois
        final DomainValue pattern = patternOperation.evaluate();

        if (pattern.isNull()) {
            return nullValue();
        }

        final String patternString = pattern.getString();

        return createJavaPattern(patternString).matcher(value.getString()).matches() ? trueValue() : falseValue();
    }

    private Pattern createJavaPattern(String patternString) {
        // TODO: il faut échapper les caractères spéciaux de regexp java dans la regexp SQL
        return Pattern.compile(patternString.replace("%", ".*"));
    }

    @Override
    public Domain domain() {
        return BOOLEAN;
    }

    @Override
    public boolean isConstant() {
        return input.isConstant() && patternOperation.isConstant();
    }
}

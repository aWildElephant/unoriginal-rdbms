package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.ColumnName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.ColumnName.columnName;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class ColumnReferenceRule {

    private ColumnReferenceRule() {

    }

    static ColumnName deriveColumnReference(Lexer lexer) {
        return columnName(consumeIdentifier(lexer));
    }
}

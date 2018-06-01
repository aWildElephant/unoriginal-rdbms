package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.TableName.tableName;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeIdentifier;

final class TableNameRule {

    private TableNameRule() {

    }

    static TableName deriveTableNameRule(Lexer lexer) {
        return tableName(consumeIdentifier(lexer));
    }
}

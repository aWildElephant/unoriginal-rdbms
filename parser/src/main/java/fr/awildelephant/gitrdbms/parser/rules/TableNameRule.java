package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.TableName;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import static fr.awildelephant.gitrdbms.ast.TableName.tableName;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeIdentifier;

public final class TableNameRule {

    private TableNameRule() {

    }

    public static TableName deriveTableNameRule(Lexer lexer) {
        return tableName(consumeIdentifier(lexer));
    }
}

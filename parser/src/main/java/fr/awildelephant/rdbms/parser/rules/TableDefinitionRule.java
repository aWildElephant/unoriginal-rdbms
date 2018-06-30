package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.CreateTable;
import fr.awildelephant.rdbms.ast.TableElementList;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.CreateTable.createTable;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableContentsSourceRule.deriveTableContentsSourceRule;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableName;

final class TableDefinitionRule {

    private TableDefinitionRule() {

    }

    static CreateTable deriveTableDefinitionRule(final Lexer lexer) {
        consumeAndExpect(CREATE, lexer);
        consumeAndExpect(TABLE, lexer);

        final TableName tableName = deriveTableName(lexer);
        final TableElementList tableContentsSource = deriveTableContentsSourceRule(lexer);

        return createTable(tableName, tableContentsSource);
    }
}

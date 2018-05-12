package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.ast.CreateTable;
import fr.awildelephant.gitrdbms.ast.TableName;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import static fr.awildelephant.gitrdbms.ast.CreateTable.createTable;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.CREATE;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.TABLE;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.TableContentsSourceRule.deriveTableContentsSourceRule;
import static fr.awildelephant.gitrdbms.parser.rules.TableNameRule.deriveTableNameRule;

public final class TableDefinitionRule {

    private TableDefinitionRule() {

    }

    public static CreateTable deriveTableDefinitionRule(final Lexer lexer) {
        consumeAndExpect(lexer, CREATE);
        consumeAndExpect(lexer, TABLE);

        final TableName tableName = deriveTableNameRule(lexer);
        final AST tableContentsSource = deriveTableContentsSourceRule(lexer);

        return createTable(tableName, tableContentsSource);
    }
}

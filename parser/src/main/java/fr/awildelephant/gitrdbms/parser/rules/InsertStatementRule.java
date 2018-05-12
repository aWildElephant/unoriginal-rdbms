package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.ast.Rows;
import fr.awildelephant.gitrdbms.ast.TableName;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import static fr.awildelephant.gitrdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INSERT;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.INTO;
import static fr.awildelephant.gitrdbms.parser.rules.FromConstructorRule.deriveFromConstructorRule;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.TableNameRule.deriveTableNameRule;

final class InsertStatementRule {

    private InsertStatementRule() {

    }

    static AST deriveInsertStatementRule(final Lexer lexer) {
        consumeAndExpect(lexer, INSERT);
        consumeAndExpect(lexer, INTO);

        final TableName tableName = deriveTableNameRule(lexer);
        final Rows insertSource = deriveFromConstructorRule(lexer);

        return insertInto(tableName, insertSource);
    }
}

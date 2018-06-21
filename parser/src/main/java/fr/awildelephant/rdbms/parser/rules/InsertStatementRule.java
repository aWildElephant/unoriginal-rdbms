package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Rows;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import static fr.awildelephant.rdbms.ast.InsertInto.insertInto;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INSERT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.INTO;
import static fr.awildelephant.rdbms.parser.rules.FromConstructorRule.deriveFromConstructorRule;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.TableNameRule.deriveTableNameRule;

final class InsertStatementRule {

    private InsertStatementRule() {

    }

    static AST deriveInsertStatementRule(final Lexer lexer) {
        consumeAndExpect(INSERT, lexer);
        consumeAndExpect(INTO, lexer);

        final TableName tableName = deriveTableNameRule(lexer);
        final Rows insertSource = deriveFromConstructorRule(lexer);

        return insertInto(tableName, insertSource);
    }
}

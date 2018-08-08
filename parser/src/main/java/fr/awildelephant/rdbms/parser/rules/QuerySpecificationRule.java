package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.Select;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Distinct.distinct;
import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.DISTINCT;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.SelectListRule.deriveSelectListRule;

final class QuerySpecificationRule {

    private QuerySpecificationRule() {

    }

    static AST deriveQuerySpecificationRule(final Lexer lexer) {
        consumeAndExpect(SELECT, lexer);

        final boolean distinct = consumeIfDistinct(lexer);

        final List<AST> outputColumns = deriveSelectListRule(lexer);

        consumeAndExpect(FROM, lexer);

        final TableName tableName = TableNameRule.deriveTableName(lexer);

        final Select select = select(outputColumns, tableName);

        if (distinct) {
            return distinct(select);
        }

        return select;
    }

    private static boolean consumeIfDistinct(Lexer lexer) {
        if (lexer.lookupNextToken().type() == DISTINCT) {
            lexer.consumeNextToken();

            return true;
        }

        return false;
    }
}

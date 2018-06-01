package fr.awildelephant.rdbms.parser.rules;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.TableName;
import fr.awildelephant.rdbms.lexer.Lexer;

import java.util.List;

import static fr.awildelephant.rdbms.ast.Select.select;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.rdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.rdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.rdbms.parser.rules.SelectListRule.deriveSelectListRule;

final class QuerySpecificationRule {

    private QuerySpecificationRule() {

    }

    static AST deriveQuerySpecificationRule(final Lexer lexer) {
        consumeAndExpect(lexer, SELECT);

        final List<AST> outputColumns = deriveSelectListRule(lexer);

        consumeAndExpect(lexer, FROM);

        final TableName tableName = TableNameRule.deriveTableNameRule(lexer);

        return select(outputColumns, tableName);
    }
}

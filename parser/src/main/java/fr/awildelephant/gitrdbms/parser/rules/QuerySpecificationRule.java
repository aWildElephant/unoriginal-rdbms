package fr.awildelephant.gitrdbms.parser.rules;

import fr.awildelephant.gitrdbms.ast.AST;
import fr.awildelephant.gitrdbms.ast.TableName;
import fr.awildelephant.gitrdbms.lexer.Lexer;

import java.util.List;

import static fr.awildelephant.gitrdbms.ast.Select.select;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.FROM;
import static fr.awildelephant.gitrdbms.lexer.tokens.TokenType.SELECT;
import static fr.awildelephant.gitrdbms.parser.rules.ParseHelper.consumeAndExpect;
import static fr.awildelephant.gitrdbms.parser.rules.SelectListRule.deriveSelectListRule;
import static fr.awildelephant.gitrdbms.parser.rules.TableNameRule.deriveTableNameRule;

final class QuerySpecificationRule {

    private QuerySpecificationRule() {

    }

    static AST deriveQuerySpecificationRule(final Lexer lexer) {
        consumeAndExpect(lexer, SELECT);

        final List<AST> outputColumns = deriveSelectListRule(lexer);

        consumeAndExpect(lexer, FROM);

        final TableName tableName = deriveTableNameRule(lexer);

        return select(outputColumns, tableName);
    }
}

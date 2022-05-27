package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;

import java.util.function.Function;

public final class ColumnNameResolver implements Function<AST, String> {

    @Override
    public String apply(AST ast) {
        final StringBuilder stringBuilder = new StringBuilder();
        new ColumnNameResolverHelper(stringBuilder).apply(ast);
        return stringBuilder.toString();
    }
}

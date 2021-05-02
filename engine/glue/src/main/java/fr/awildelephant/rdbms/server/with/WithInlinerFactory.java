package fr.awildelephant.rdbms.server.with;

import fr.awildelephant.rdbms.ast.AST;

import java.util.function.Function;

public final class WithInlinerFactory {

    public <T> WithInliner<T> build(Function<AST, T> resultHandler) {
        return new WithInliner<>(resultHandler);
    }
}

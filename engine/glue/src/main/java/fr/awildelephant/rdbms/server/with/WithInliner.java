package fr.awildelephant.rdbms.server.with;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.WithElement;
import fr.awildelephant.rdbms.ast.WithList;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class WithInliner<T> implements Function<With, T> {

    private final Function<AST, T> resultHandler;

    public WithInliner(Function<AST, T> resultHandler) {
        this.resultHandler = resultHandler;
    }

    @Override
    public T apply(With with) {
        final WithQueryReplacer replacer = new WithQueryReplacer(buildElementsMap(with.withList()));

        return replacer.andThen(resultHandler).apply(with.query());
    }

    private static Map<String, AST> buildElementsMap(WithList withList) {
        final Map<String, AST> elementsMap = new HashMap<>();

        for (WithElement element : withList.elements()) {
            if (elementsMap.containsKey(element.name())) {
                throw new IllegalArgumentException("Duplicate query name '" + element.name() + "' in with clause");
            }

            elementsMap.put(element.name(), element.definition());
        }

        return elementsMap;
    }
}

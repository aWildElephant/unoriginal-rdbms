package fr.awildelephant.rdbms.server.dispatch.executor;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.WithElement;
import fr.awildelephant.rdbms.ast.WithList;
import fr.awildelephant.rdbms.database.version.Version;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.server.with.WithQueryReplacer;

import java.util.HashMap;
import java.util.Map;

public final class WithExecutor {

    private final ReadQueryExecutor readQueryExecutor;

    public WithExecutor(ReadQueryExecutor readQueryExecutor) {
        this.readQueryExecutor = readQueryExecutor;
    }

    public Table execute(With with, Version readVersion) {
        final WithQueryReplacer replacer = new WithQueryReplacer(buildElementsMap(with.withList()));

        return readQueryExecutor.execute(replacer.apply(with.query()), readVersion);
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

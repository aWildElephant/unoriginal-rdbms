package fr.awildelephant.rdbms.ast.builder;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.With;
import fr.awildelephant.rdbms.ast.WithElement;
import fr.awildelephant.rdbms.ast.WithList;
import fr.awildelephant.rdbms.utils.common.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WithBuilder implements Builder<With> {

    private final Map<String, AST> elements = new HashMap<>();
    private AST query;

    private WithBuilder() {

    }

    public static WithBuilder with() {
        return new WithBuilder();
    }

    public WithBuilder with(String name, Builder<? extends AST> queryBuilder) {
        return with(name, queryBuilder.build());
    }

    public WithBuilder with(String name, AST query) {
        elements.put(name, query);
        return this;
    }

    public WithBuilder query(Builder<? extends AST> queryBuilder) {
        return query(queryBuilder.build());
    }

    private WithBuilder query(AST query) {
        this.query = query;
        return this;
    }

    @Override
    public With build() {
        final List<WithElement> elementList = elements.entrySet().stream().map(entry -> new WithElement(entry.getKey(), entry.getValue())).toList();
        return new With(new WithList(elementList), query);
    }
}

package fr.awildelephant.rdbms.ast.builder;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.CreateAssertion;
import fr.awildelephant.rdbms.utils.common.Builder;

public class CreateAssertionBuilder implements Builder<CreateAssertion> {

    private final String name;
    private AST predicate;

    private CreateAssertionBuilder(String name) {
        this.name = name;
    }

    public static CreateAssertionBuilder createAssertion(String name) {
        return new CreateAssertionBuilder(name);
    }

    public CreateAssertionBuilder check(Builder<AST> predicateBuilder) {
        return check(predicateBuilder.build());
    }

    public CreateAssertionBuilder check(AST predicate) {
        this.predicate = predicate;
        return this;
    }

    @Override
    public CreateAssertion build() {
        return new CreateAssertion(name, predicate);
    }
}

package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class FunctionCall implements AST {

    private final String name;
    private final List<AST> parameters;

    private FunctionCall(String name, List<AST> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public static FunctionCall functionCall(String name, List<AST> parameters) {
        return new FunctionCall(name, parameters);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return null;
    }

    @Override
    public Collection<? extends AST> children() {
        return parameters;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final FunctionCall other)) {
            return false;
        }

        return Objects.equals(name, other.name)
                && Objects.equals(parameters, other.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, parameters);
    }
}

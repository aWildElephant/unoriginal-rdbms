package fr.awildelephant.rdbms.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class IdentifierChain implements AST {

    private final List<String> identifiers;

    private IdentifierChain(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public static IdentifierChain identifierChain(String identifier) {
        return new IdentifierChain(List.of(identifier));
    }

    public static IdentifierChainBuilder builder() {
        return new IdentifierChainBuilder();
    }

    public List<String> identifiers() {
        return identifiers;
    }

    public String last() {
        return identifiers.get(identifiers.size() - 1);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifiers);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IdentifierChain)) {
            return false;
        }

        final IdentifierChain other = (IdentifierChain) obj;

        return Objects.equals(identifiers, other.identifiers);
    }

    public static final class IdentifierChainBuilder {

        private final List<String> identifiers = new ArrayList<>();

        public void add(String identifier) {
            identifiers.add(identifier);
        }

        public IdentifierChain build() {
            return new IdentifierChain(identifiers);
        }
    }
}

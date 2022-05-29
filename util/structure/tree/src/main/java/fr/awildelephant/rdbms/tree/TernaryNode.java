package fr.awildelephant.rdbms.tree;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class TernaryNode<T extends Tree<T>, C1 extends T, C2 extends T, C3 extends T> implements Tree<T> {

    private final C1 firstChild;
    private final C2 secondChild;
    private final C3 thirdChild;

    protected TernaryNode(C1 firstChild, C2 secondChild, C3 thirdChild) {
        this.firstChild = firstChild;
        this.secondChild = secondChild;
        this.thirdChild = thirdChild;
    }

    public C1 firstChild() {
        return firstChild;
    }

    public C2 secondChild() {
        return secondChild;
    }

    public C3 thirdChild() {
        return thirdChild;
    }

    @Override
    public Collection<? extends T> children() {
        return List.of(firstChild, secondChild, thirdChild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstChild, secondChild, thirdChild);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TernaryNode<?, ?, ?, ?> other)) {
            return false;
        }

        return equalsTernary(other);
    }

    protected boolean equalsTernary(TernaryNode<?, ?, ?, ?> other) {
        return Objects.equals(firstChild, other.firstChild)
                && Objects.equals(secondChild, other.secondChild)
                && Objects.equals(thirdChild, other.thirdChild);
    }
}

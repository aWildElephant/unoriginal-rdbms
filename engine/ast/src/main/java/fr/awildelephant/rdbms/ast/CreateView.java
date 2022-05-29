package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.List;
import java.util.Objects;

public final class CreateView extends UnaryNode<AST, AST> implements AST {

    private final String name;
    private final List<String> columnNames;

    public CreateView(String name, List<String> columnNames, AST query) {
        super(query);
        this.name = name;
        this.columnNames = columnNames;
    }

    public static CreateView createView(String name, List<String> columnNames, AST query) {
        return new CreateView(name, columnNames, query);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String name() {
        return name;
    }

    public List<String> columnNames() {
        return columnNames;
    }

    public AST query() {
        return child();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreateView other)) {
            return false;
        }

        return Objects.equals(name, other.name)
                && Objects.equals(columnNames, other.columnNames)
                && equalsUnaryNode(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columnNames, child());
    }

    @Override
    public String toString() {
        return "CreateView[name=" + name + ", columnNames=" + columnNames + ", query=" + child() + ']';
    }

}

package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;

public final class CreateView implements AST {
    private final String name;

    private final List<String> columnNames;
    private final AST query;

    private CreateView(String name, List<String> columnNames, AST query) {
        this.name = name;
        this.columnNames = columnNames;
        this.query = query;
    }

    public static CreateView createView(String name, List<String> columnNames, AST query) {
        return new CreateView(name, columnNames, query);
    }

    public String name() {
        return name;
    }

    public List<String> columnNames() {
        return columnNames;
    }

    public AST query() {
        return query;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columnNames, query);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final CreateView other)) {
            return false;
        }

        return Objects.equals(name, other.name)
                && Objects.equals(columnNames, other.columnNames)
                && Objects.equals(query, other.query);
    }
}

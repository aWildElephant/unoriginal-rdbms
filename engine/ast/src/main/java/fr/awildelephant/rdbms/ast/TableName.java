package fr.awildelephant.rdbms.ast;

import java.util.Objects;

public final class TableName implements AST {

    private final String name;

    private TableName(String name) {
        this.name = name;
    }

    public static TableName tableName(String name) {
        return new TableName(name);
    }

    public String name() {
        return name;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableName)) {
            return false;
        }

        final TableName other = (TableName) obj;

        return Objects.equals(name, other.name);
    }
}

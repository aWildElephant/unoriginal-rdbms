package fr.awildelephant.rdbms.ast;

public final class ColumnName implements AST {

    private final String name;

    private ColumnName(String name) {
        this.name = name;
    }

    public static ColumnName columnName(String name) {
        return new ColumnName(name);
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
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnName)) {
            return false;
        }

        final ColumnName other = (ColumnName) obj;

        return name.equals(other.name);
    }
}

package fr.awildelephant.rdbms.ast;

public final class TableName implements AST {

    private final String name;

    private TableName(String name) {
        this.name = name;
    }

    public static TableName tableName(String name) {
        return new TableName(name);
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
        if (!(obj instanceof TableName)) {
            return false;
        }

        final TableName other = (TableName) obj;

        return name.equals(other.name);
    }
}

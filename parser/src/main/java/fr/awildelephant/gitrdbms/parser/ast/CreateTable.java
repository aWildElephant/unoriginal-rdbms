package fr.awildelephant.gitrdbms.parser.ast;

public final class CreateTable implements AST {

    private final String tableName;

    /**
     * @param tableName must be not null
     */
    public CreateTable(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return tableName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreateTable)) {
            return false;
        }

        final CreateTable other = (CreateTable) obj;

        return tableName.equals(other.tableName);
    }
}

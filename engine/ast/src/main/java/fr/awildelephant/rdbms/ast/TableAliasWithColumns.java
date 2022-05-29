package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;
import fr.awildelephant.rdbms.tree.UnaryNode;

import java.util.List;
import java.util.Objects;

public final class TableAliasWithColumns extends UnaryNode<AST, AST> implements AST {

    private final String tableAlias;
    private final List<String> columnAliases;

    public TableAliasWithColumns(AST child, String tableAlias, List<String> columnAliases) {
        super(child);
        this.tableAlias = tableAlias;
        this.columnAliases = columnAliases;
    }

    public static TableAliasWithColumns tableAliasWithColumns(AST child, String tableAlias, List<String> columnAliases) {
        return new TableAliasWithColumns(child, tableAlias, columnAliases);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String tableAlias() {
        return tableAlias;
    }

    public List<String> columnAliases() {
        return columnAliases;
    }

    @Override
    public String toString() {
        return "TableAliasWithColumns[" +
                "input=" + child() + ", " +
                "tableAlias=" + tableAlias + ", " +
                "columnAliases=" + columnAliases + ']';
    }

    @Override
    public int hashCode() {
        return Objects.hash(child(), tableAlias, columnAliases);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TableAliasWithColumns other)) {
            return false;
        }

        return Objects.equals(tableAlias, other.tableAlias)
                && Objects.equals(columnAliases, other.columnAliases)
                && equalsUnaryNode(other);
    }

}

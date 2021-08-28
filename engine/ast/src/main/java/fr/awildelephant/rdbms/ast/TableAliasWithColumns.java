package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.List;
import java.util.Objects;

public final class TableAliasWithColumns implements AST {

    private final AST input;
    private final String tableAlias;
    private final List<String> columnAliases;

    private TableAliasWithColumns(AST input, String tableAlias, List<String> columnAliases) {
        this.input = input;
        this.tableAlias = tableAlias;
        this.columnAliases = columnAliases;
    }

    public static TableAliasWithColumns tableAliasWithColumns(AST input, String tableAlias, List<String> columnAliases) {
        return new TableAliasWithColumns(input, tableAlias, columnAliases);
    }

    public AST input() {
        return input;
    }

    public String tableAlias() {
        return tableAlias;
    }

    public List<String> columnAliases() {
        return columnAliases;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, tableAlias, columnAliases);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final TableAliasWithColumns other)) {
            return false;
        }

        return Objects.equals(input, other.input)
                && Objects.equals(tableAlias, other.tableAlias)
                && Objects.equals(columnAliases, other.columnAliases);
    }
}

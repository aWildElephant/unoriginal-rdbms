package fr.awildelephant.rdbms.ast;

import fr.awildelephant.rdbms.ast.visitor.ASTVisitor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class ReadCSV implements AST { //NAryASTNode

    private final String filePath;
    private final List<ColumnDefinition> columns;

    private ReadCSV(String filePath, List<ColumnDefinition> columns) {
        this.filePath = filePath;
        this.columns = columns;
    }

    public static ReadCSV readCSV(String filePath, List<ColumnDefinition> columns) {
        return new ReadCSV(filePath, columns);
    }

    public String filePath() {
        return filePath;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Collection<ColumnDefinition> children() {
        return columns;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final ReadCSV other)) {
            return false;
        }

        return Objects.equals(filePath, other.filePath)
                && Objects.equals(columns, other.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, columns);
    }

    @Override
    public String toString() {
        return "ReadCSV[filePath='" + filePath + "',values=" + children() + ']';
    }
}

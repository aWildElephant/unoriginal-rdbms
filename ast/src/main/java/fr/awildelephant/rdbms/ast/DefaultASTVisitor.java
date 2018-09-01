package fr.awildelephant.rdbms.ast;

public abstract class DefaultASTVisitor<T> implements ASTVisitor<T> {

    @Override
    public T visit(Asterisk asterisk) {
        return defaultVisit(asterisk);
    }

    @Override
    public T visit(Cast cast) {
        return defaultVisit(cast);
    }

    @Override
    public T visit(ColumnAlias columnAlias) {
        return defaultVisit(columnAlias);
    }

    @Override
    public T visit(ColumnDefinition columnDefinition) {
        return defaultVisit(columnDefinition);
    }

    @Override
    public T visit(ColumnName columnName) {
        return defaultVisit(columnName);
    }

    @Override
    public T visit(CreateTable createTable) {
        return defaultVisit(createTable);
    }

    @Override
    public T visit(Distinct distinct) {
        return defaultVisit(distinct);
    }

    @Override
    public T visit(DropTable dropTable) {
        return defaultVisit(dropTable);
    }

    @Override
    public T visit(InsertInto insertInto) {
        return defaultVisit(insertInto);
    }

    @Override
    public T visit(Row row) {
        return defaultVisit(row);
    }

    @Override
    public T visit(Rows rows) {
        return defaultVisit(rows);
    }

    @Override
    public T visit(Select select) {
        return defaultVisit(select);
    }

    @Override
    public T visit(TableElementList tableElementList) {
        return defaultVisit(tableElementList);
    }

    @Override
    public T visit(TableName tableName) {
        return defaultVisit(tableName);
    }

    @Override
    public T visit(Value value) {
        return defaultVisit(value);
    }

    public abstract T defaultVisit(AST node);
}

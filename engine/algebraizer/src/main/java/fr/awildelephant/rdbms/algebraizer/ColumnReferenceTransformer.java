package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.ast.DefaultASTVisitor;
import fr.awildelephant.rdbms.ast.QualifiedColumnName;
import fr.awildelephant.rdbms.ast.UnqualifiedColumnName;
import fr.awildelephant.rdbms.schema.ColumnReference;
import fr.awildelephant.rdbms.schema.QualifiedColumnReference;
import fr.awildelephant.rdbms.schema.UnqualifiedColumnReference;

public final class ColumnReferenceTransformer extends DefaultASTVisitor<ColumnReference> {

    private final ColumnNameResolver columnNameResolver;

    ColumnReferenceTransformer(ColumnNameResolver columnNameResolver) {
        this.columnNameResolver = columnNameResolver;
    }

    @Override
    public ColumnReference visit(QualifiedColumnName qualifiedColumnReference) {
        return new QualifiedColumnReference(qualifiedColumnReference.qualifier(), qualifiedColumnReference.name());
    }

    @Override
    public ColumnReference visit(UnqualifiedColumnName unqualifiedColumnReference) {
        return new UnqualifiedColumnReference(unqualifiedColumnReference.name());
    }

    @Override
    public ColumnReference defaultVisit(AST node) {
        return new UnqualifiedColumnReference(columnNameResolver.apply(node));
    }
}

package fr.awildelephant.rdbms.algebraizer;

import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.database.StorageSnapshot;
import fr.awildelephant.rdbms.version.Version;

public final class AlgebraizerFactory {

    private final AliasExtractor aliasExtractor;
    private final ExpressionSplitter expressionSplitter;
    private final ColumnNameResolver columnNameResolver;
    private final ColumnReferenceTransformer columnReferenceTransformer;
    private final Storage storage;

    public AlgebraizerFactory(Storage storage, AliasExtractor aliasExtractor, ExpressionSplitter expressionSplitter, ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer) {
        this.storage = storage;
        this.aliasExtractor = aliasExtractor;
        this.expressionSplitter = expressionSplitter;
        this.columnNameResolver = columnNameResolver;
        this.columnReferenceTransformer = columnReferenceTransformer;
    }

    public Algebraizer build(Version version) {
        final StorageSnapshot storageSnapshot = new StorageSnapshot(storage, version);
        return new Algebraizer(storageSnapshot, columnNameResolver, columnReferenceTransformer, expressionSplitter, aliasExtractor);
    }
}

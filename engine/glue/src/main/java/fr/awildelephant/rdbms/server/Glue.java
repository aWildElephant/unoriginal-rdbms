package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.AlgebraizerFactory;
import fr.awildelephant.rdbms.algebraizer.AliasExtractor;
import fr.awildelephant.rdbms.algebraizer.ColumnNameResolver;
import fr.awildelephant.rdbms.algebraizer.ColumnReferenceTransformer;
import fr.awildelephant.rdbms.algebraizer.ExpressionSplitter;
import fr.awildelephant.rdbms.database.MVCC;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.execution.plan.PlanFactory;
import fr.awildelephant.rdbms.explain.ExplanationTableBuilder;
import fr.awildelephant.rdbms.explain.ExplanationTreeBuilder;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.server.dispatch.QueryDispatcher;
import fr.awildelephant.rdbms.server.dispatch.executor.CreateTableExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.CreateViewExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.DeleteExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.DropTableExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.ExplainExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.InsertIntoExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.ReadQueryExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.TruncateExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.WithExecutor;
import fr.awildelephant.rdbms.storage.data.table.Table;
import io.activej.inject.Injector;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;

@SuppressWarnings("unused")
public final class Glue {

    private final QueryExecutor executor;

    public Glue() {
        final Injector injector = Injector.of(new DatabaseModule(), new AlgebraizerModule(), new OptimizerModule(), new PlanModule(), new ExplainModule(), new GlueModule());

        executor = injector.getInstance(QueryExecutor.class);
    }

    public Table execute(final String query) {
        return executor.execute(query);
    }

    public static class DatabaseModule extends AbstractModule {

        @Provides
        MVCC mvcc() {
            return new MVCC();
        }

        @Provides
        Storage storage() {
            return new Storage();
        }
    }

    public static class AlgebraizerModule extends AbstractModule {

        @Provides
        ColumnNameResolver columnNameResolver() {
            return new ColumnNameResolver();
        }

        @Provides
        ColumnReferenceTransformer columnReferenceTransformer(ColumnNameResolver columnNameResolver) {
            return new ColumnReferenceTransformer(columnNameResolver);
        }

        @Provides
        ExpressionSplitter expressionSplitter(ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer) {
            return new ExpressionSplitter(columnNameResolver, columnReferenceTransformer);
        }

        @Provides
        AliasExtractor aliasExtractor(ColumnReferenceTransformer columnReferenceTransformer) {
            return new AliasExtractor(columnReferenceTransformer);
        }

        @Provides
        AlgebraizerFactory algebraizerFactory(Storage storage, ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer, ExpressionSplitter expressionSplitter, AliasExtractor aliasExtractor) {
            return new AlgebraizerFactory(storage, aliasExtractor, expressionSplitter, columnNameResolver, columnReferenceTransformer);
        }
    }

    public static class OptimizerModule extends AbstractModule {

        @Provides
        Optimizer optimizer() {
            return new Optimizer();
        }
    }

    public static class PlanModule extends AbstractModule {

        @Provides
        PlanFactory planFactory() {
            return new PlanFactory();
        }
    }

    public static class ExplainModule extends AbstractModule {

        @Provides
        ExplanationTreeBuilder explanationTreeBuilder() {
            return new ExplanationTreeBuilder();
        }

        @Provides
        ExplanationTableBuilder explanationTableBuilder() {
            return new ExplanationTableBuilder();
        }

        @Provides
        LogicalPlanTableBuilder logicalPlanTableBuilder(ExplanationTreeBuilder explanationTreeBuilder, ExplanationTableBuilder explanationTableBuilder) {
            return new LogicalPlanTableBuilder(explanationTreeBuilder, explanationTableBuilder);
        }
    }

    public static class GlueModule extends AbstractModule {

        @Provides
        CreateTableExecutor createTableExecutor(Storage storage) {
            return new CreateTableExecutor(storage);
        }

        @Provides
        CreateViewExecutor createViewExecutor(AlgebraizerFactory algebraizerFactory, Storage storage) {
            return new CreateViewExecutor(algebraizerFactory, storage);
        }

        @Provides
        DeleteExecutor deleteExecutor(Storage storage) {
            return new DeleteExecutor(storage);
        }

        @Provides
        DropTableExecutor dropTableExecutor(Storage storage) {
            return new DropTableExecutor(storage);
        }

        @Provides
        ExplainExecutor explainExecutor(AlgebraizerFactory algebraizerFactory, LogicalPlanTableBuilder logicalPlanTableBuilder, Optimizer optimizer) {
            return new ExplainExecutor(algebraizerFactory, logicalPlanTableBuilder, optimizer);
        }

        @Provides
        InsertIntoExecutor insertIntoExecutor(ReadQueryExecutor readQueryExecutor, Storage storage) {
            return new InsertIntoExecutor(readQueryExecutor, storage);
        }

        @Provides
        ReadQueryExecutor readQueryExecutor(AlgebraizerFactory algebraizerFactory, Optimizer optimizer, PlanFactory planFactory, Storage storage) {
            return new ReadQueryExecutor(algebraizerFactory, optimizer, planFactory, storage);
        }

        @Provides
        TruncateExecutor truncateExecutor(Storage storage) {
            return new TruncateExecutor(storage);
        }

        @Provides
        WithExecutor withExecutor(ReadQueryExecutor readQueryExecutor) {
            return new WithExecutor(readQueryExecutor);
        }

        @Provides
        QueryDispatcher queryDispatcher(CreateTableExecutor createTableExecutor, CreateViewExecutor createViewExecutor,
                                        DeleteExecutor deleteExecutor, DropTableExecutor dropTableExecutor,
                                        ExplainExecutor explainExecutor, InsertIntoExecutor insertIntoExecutor,
                                        ReadQueryExecutor readQueryExecutor, WithExecutor withExecutor,
                                        TruncateExecutor truncateExecutor) {
            return new QueryDispatcher(createTableExecutor, createViewExecutor, deleteExecutor, dropTableExecutor,
                    explainExecutor, insertIntoExecutor, readQueryExecutor, withExecutor, truncateExecutor);
        }

        @Provides
        TransactionManager transactionManager() {
            return new TransactionManager();
        }

        @Provides
        QueryExecutor concurrentQueryExecutor(QueryDispatcher queryDispatcher, MVCC mvcc, TransactionManager transactionManager) {
            return new QueryExecutor(queryDispatcher, mvcc, transactionManager);
        }
    }
}

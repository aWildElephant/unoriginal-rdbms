package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.Algebraizer;
import fr.awildelephant.rdbms.algebraizer.AliasExtractor;
import fr.awildelephant.rdbms.algebraizer.ColumnNameResolver;
import fr.awildelephant.rdbms.algebraizer.ColumnReferenceTransformer;
import fr.awildelephant.rdbms.algebraizer.ExpressionSplitter;
import fr.awildelephant.rdbms.database.MVCC;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.execution.plan.PlanFactory;
import fr.awildelephant.rdbms.explain.ExplanationTableBuilder;
import fr.awildelephant.rdbms.explain.ExplanationTreeBuilder;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.server.dispatch.QueryDispatcher;
import fr.awildelephant.rdbms.server.dispatch.executor.CreateTableExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.CreateViewExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.DropTableExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.ExplainExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.InsertIntoExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.ReadQueryExecutor;
import fr.awildelephant.rdbms.server.dispatch.executor.WithExecutor;
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
        Algebraizer algebraizer(Storage storage, ColumnNameResolver columnNameResolver, ColumnReferenceTransformer columnReferenceTransformer, ExpressionSplitter expressionSplitter, AliasExtractor aliasExtractor) {
            return new Algebraizer(storage, columnNameResolver, columnReferenceTransformer, expressionSplitter, aliasExtractor);
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
        CreateViewExecutor createViewExecutor(Algebraizer algebraizer, Storage storage) {
            return new CreateViewExecutor(algebraizer, storage);
        }

        @Provides
        DropTableExecutor dropTableExecutor(Storage storage) {
            return new DropTableExecutor(storage);
        }

        @Provides
        ExplainExecutor explainExecutor(Algebraizer algebraizer, LogicalPlanTableBuilder logicalPlanTableBuilder, Optimizer optimizer) {
            return new ExplainExecutor(algebraizer, logicalPlanTableBuilder, optimizer);
        }

        @Provides
        InsertIntoExecutor insertIntoExecutor(ReadQueryExecutor readQueryExecutor, Storage storage) {
            return new InsertIntoExecutor(readQueryExecutor, storage);
        }

        @Provides
        ReadQueryExecutor readQueryExecutor(Algebraizer algebraizer, Optimizer optimizer, PlanFactory planFactory, Storage storage) {
            return new ReadQueryExecutor(algebraizer, optimizer, planFactory, storage);
        }

        @Provides
        WithExecutor withExecutor(ReadQueryExecutor readQueryExecutor) {
            return new WithExecutor(readQueryExecutor);
        }

        @Provides
        QueryDispatcher queryDispatcher(CreateTableExecutor createTableExecutor, CreateViewExecutor createViewExecutor, DropTableExecutor dropTableExecutor, ExplainExecutor explainExecutor, InsertIntoExecutor insertIntoExecutor, ReadQueryExecutor readQueryExecutor, WithExecutor withExecutor) {
            return new QueryDispatcher(createTableExecutor, createViewExecutor, dropTableExecutor, explainExecutor, insertIntoExecutor, readQueryExecutor, withExecutor);
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

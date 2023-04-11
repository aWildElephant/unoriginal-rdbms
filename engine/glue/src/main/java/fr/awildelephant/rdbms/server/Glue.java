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
import fr.awildelephant.rdbms.server.with.WithInlinerFactory;
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
        WithInlinerFactory withInlinerFactory() {
            return new WithInlinerFactory();
        }

        @Provides
        QueryDispatcherOld queryDispatcher(Storage storage, Algebraizer algebraizer, Optimizer optimizer, WithInlinerFactory withInlinerFactory, LogicalPlanTableBuilder logicalPlanTableBuilder, PlanFactory planFactory) {
            return new QueryDispatcherOld(storage, algebraizer, optimizer, withInlinerFactory, logicalPlanTableBuilder, planFactory);
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

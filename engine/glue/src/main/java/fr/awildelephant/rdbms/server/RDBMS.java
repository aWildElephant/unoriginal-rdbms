package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.algebraizer.*;
import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.database.Storage;
import fr.awildelephant.rdbms.engine.data.table.Table;
import fr.awildelephant.rdbms.engine.optimizer.Optimizer;
import fr.awildelephant.rdbms.explain.ExplanationTableBuilder;
import fr.awildelephant.rdbms.explain.ExplanationTreeBuilder;
import fr.awildelephant.rdbms.explain.LogicalPlanTableBuilder;
import fr.awildelephant.rdbms.lexer.Lexer;
import fr.awildelephant.rdbms.parser.Parser;
import fr.awildelephant.rdbms.server.with.WithInlinerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fr.awildelephant.rdbms.lexer.InputStreamWrapper.wrap;
import static java.lang.System.currentTimeMillis;

public final class RDBMS {

    private static final Logger LOGGER = LogManager.getLogger("Monitoring");

    private final ConcurrentQueryExecutor executor;

    public RDBMS() {
        final Storage storage = new Storage();
        final Algebraizer algebraizer = buildAlgebraizerAndDependencies(storage);
        final Optimizer optimizer = new Optimizer();
        final WithInlinerFactory withInlinerFactory = new WithInlinerFactory();
        final ExplanationTableBuilder explanationTableBuilder = new ExplanationTableBuilder();
        final ExplanationTreeBuilder explanationTreeBuilder = new ExplanationTreeBuilder();
        final LogicalPlanTableBuilder logicalPlanTableBuilder = new LogicalPlanTableBuilder(explanationTreeBuilder, explanationTableBuilder);
        final QueryDispatcher dispatcher = new QueryDispatcher(storage, algebraizer, optimizer, withInlinerFactory, logicalPlanTableBuilder);
        executor = new ConcurrentQueryExecutor(dispatcher);
    }

    private Algebraizer buildAlgebraizerAndDependencies(Storage storage) {
        final ColumnNameResolver columnNrmeResolver = new ColumnNameResolver();
        final ColumnReferenceTransformer columnReferenceTransformer = new ColumnReferenceTransformer(columnNrmeResolver);
        final ExpressionSplitter expressionSplitter = new ExpressionSplitter(columnNrmeResolver, columnReferenceTransformer);
        final AliasExtractor aliasExtractor = new AliasExtractor(columnReferenceTransformer);
        return new Algebraizer(storage, columnNrmeResolver, columnReferenceTransformer, expressionSplitter, aliasExtractor);
    }

    private static AST parse(final String query) {
        return new Parser(new Lexer(wrap(query))).parse();
    }

    public Table execute(final String query) {
        LOGGER.trace("Executing \"{}\"", query);
        final long start = currentTimeMillis();
        final AST ast = parse(query);
        LOGGER.trace("Took {}ms to parse", currentTimeMillis() - start);

        return executor.execute(ast);
    }
}

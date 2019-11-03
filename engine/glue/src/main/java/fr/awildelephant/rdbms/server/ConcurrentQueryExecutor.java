package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.ast.AST;
import fr.awildelephant.rdbms.engine.data.table.Table;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class ConcurrentQueryExecutor {

    private final QueryDispatcher dispatcher;
    private final Lock lock = new ReentrantLock();

    ConcurrentQueryExecutor(QueryDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public Table execute(AST query) {
        lock.lock();
        try {
            return dispatcher.apply(query);
        } finally {
            lock.unlock();
        }
    }
}

package fr.awildelephant.rdbms.server;

import fr.awildelephant.rdbms.version.TransactionId;

import java.util.concurrent.atomic.AtomicReference;

public class TransactionManager {

    private final AtomicReference<TransactionId> currentReference = new AtomicReference<>(new TransactionId(Long.MIN_VALUE));

    public TransactionId generate() {
        return currentReference.getAndUpdate(current -> new TransactionId(current.value() + 1));
    }
}

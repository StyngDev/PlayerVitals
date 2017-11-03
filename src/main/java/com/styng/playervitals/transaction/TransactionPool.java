package com.styng.playervitals.transaction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionPool {

    public static TransactionPool getInstance() {
        return TransactionPoolHelper.INSTANCE;
    }

    // Thread-Safe Singleton pattern
    private static class TransactionPoolHelper {

        private static TransactionPool INSTANCE = new TransactionPool();

    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private TransactionPool() {
    }

    public void commitTransaction(Transaction transaction) {
        executorService.submit(transaction::commit);
    }

    public void runUnsafeTask(UnsafeAction action) {
        executorService.submit(() -> {
            try {
                action.perform();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

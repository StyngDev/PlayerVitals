package com.styng.playervitals.transaction.wrappers;

import com.styng.playervitals.transaction.TransactionState;
import com.styng.playervitals.transaction.Action;
import com.styng.playervitals.transaction.Transaction;

public class ActionWrapper {

    private final Action action;
    private final Transaction transaction;
    private TransactionState state = TransactionState.WAITING;

    public ActionWrapper(Action action, Transaction transaction) {
        this.action = action;
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Action getAction() {
        return action;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }
}

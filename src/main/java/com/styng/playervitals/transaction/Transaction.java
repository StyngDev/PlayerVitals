package com.styng.playervitals.transaction;

import com.styng.playervitals.exception.Exceptionable;
import com.styng.playervitals.transaction.wrappers.ActionWrapper;

import java.util.ArrayList;
import java.util.Collection;

public class Transaction implements Exceptionable<Throwable> {

    private final Collection<ActionWrapper> actions = new ArrayList<>();
    private Throwable failureException;

    protected boolean commit() {
        for (ActionWrapper wrapper : actions) {
            try {
                wrapper.getAction().execute();
            } catch (Exception e) {
                this.failureException = e;
                wrapper.setState(TransactionState.EXCEPTION);
                rollback();
                return false;
            }
            wrapper.setState(TransactionState.EXECUTED);
        }
        return true;
    }

    protected void rollback() {
        actions.stream().filter(wrapper -> wrapper.getState() == TransactionState.EXECUTED || wrapper.getState() == TransactionState.EXCEPTION)
                .forEach(wrapper -> {
                    try {
                        wrapper.getAction().rollback();
                        wrapper.setState(TransactionState.ROLLED_BACK);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }

    public Collection<ActionWrapper> getActions() {
        return actions;
    }

    public Transaction add(Action action) {
        this.actions.add(new ActionWrapper(action, this));
        return this;
    }

    @Override
    public Throwable getException() {
        return failureException;
    }
}

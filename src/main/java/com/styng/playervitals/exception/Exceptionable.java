package com.styng.playervitals.exception;

public interface Exceptionable<T extends Throwable> {

    /**
     * <p>Used to retrieve an exception that occurred and has been stored in a subclass</p>
     * <p>In most cases this should be the last error that occurred or the error that caused a specific action to fail</p>
     *
     * @return Returns the exception that occurred
     */
    T getException();

}

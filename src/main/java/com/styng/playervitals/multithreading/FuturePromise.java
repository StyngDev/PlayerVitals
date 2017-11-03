package com.styng.playervitals.multithreading;

import com.styng.playervitals.exception.Exceptionable;

import java.util.concurrent.TimeUnit;

public interface FuturePromise<T> extends Exceptionable<Throwable> {

    /**
     * The default timeout for a future promise that will be used if no specific timeout has been set.
     */
    long DEFAULT_TIME_OUT_MILLIS = TimeUnit.MINUTES.toMillis(1);

    /**
     * <p>Locks the current thread while awaiting the promised to be fulfilled</p>
     * <p>Once the promise has been fulfilled, it will unlock the current thread</p>
     */
    void join();

    /**
     * <p>Forces the current thread to unlock and stop the {@link #join() join} action</p>
     */
    void unlock();

    /**
     * <p>Wait for the future promise to be fulfilled and returns the promised value</p>
     *
     * @return Returns the promised value
     */
    T value();

    /**
     * <p>Checks whether the promise has been fulfilled.</p>
     *
     * @return Returns whether the promise has been fulfilled
     */
    boolean isFulfilled();

    /**
     * <p>Should be called when the promise has been fulfilled without any errors.</p>
     * <p>This will unlock the current thread, if the join method has been called</p>
     *
     * @param value The promised value
     */
    void success(T value);

    /**
     * <p>Sets the success callback for this future promise. This callback will be called when the promise
     * has been fulfilled without any exceptions</p>
     *
     * @param successCallback The success callback
     *
     * @return Returns the same instance of the future promise
     *
     * @see FuturePromise#success(Object)
     */
    FuturePromise<T> setSuccessCallback(PromiseCallback<T> successCallback);

    /**
     * <p>Sets the error callback for this future promise. This callback will be called when an error occurred
     * while trying to fulfill the promise.</p>
     *
     * @param errorCallback The error callback
     *
     * @return Returns the same instance of the future promise
     *
     * @see FuturePromise#error(Throwable)
     */
    FuturePromise<T> setErrorCallback(PromiseCallback<Throwable> errorCallback);

    /**
     * <p>{@link FuturePromise#setTimeout(long)}</p>
     *
     * @param amount   The amount of the time unit
     * @param timeUnit The time unit (i.e. hours, minutes, etc.)
     *
     * @see FuturePromise#setTimeout(long)
     */
    default void setTimeout(int amount, TimeUnit timeUnit) {
        setTimeout(timeUnit.toMillis(amount));
    }

    /**
     * <p>Sets the timeout of the promise in milliseconds. If a future promise can't be fulfilled within
     * the given timeout an error will be called.</p>
     *
     * @param timeOutInMillis The timeout in milliseconds
     */
    void setTimeout(long timeOutInMillis);

    /**
     * <p>Should be called when an error occurred while trying to fulfill the promise.</p>
     * <p>This will unlock the current thread, if the join method has been called</p>
     *
     * @param error The error that occurred
     */
    void error(Throwable error);

}

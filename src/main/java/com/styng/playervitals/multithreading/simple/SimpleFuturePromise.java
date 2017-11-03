package com.styng.playervitals.multithreading.simple;

import com.styng.playervitals.multithreading.PromiseCallback;
import com.styng.playervitals.multithreading.FuturePromise;
import com.styng.playervitals.multithreading.PromisedValue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimpleFuturePromise<T> implements FuturePromise<T> {

    private PromisedValue<T> promisedValue;
    private PromiseCallback<T> successCallback;
    private PromiseCallback<Throwable> errorCallback;
    private CountDownLatch countDownLatch;
    private long timeOut = DEFAULT_TIME_OUT_MILLIS;
    private Throwable error;

    @Override
    public void join() {
        if (!isFulfilled() && countDownLatch == null) {
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await(timeOut, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                error(e);
            }
        }
    }

    @Override
    public void unlock() {
        if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
            this.countDownLatch = null;
        }
    }

    @Override
    public T value() {
        join();
        return promisedValue != null ? promisedValue.getValue() : null;
    }

    @Override
    public boolean isFulfilled() {
        return promisedValue != null;
    }

    @Override
    public void success(T value) {
        if (!isFulfilled()) {
            promisedValue = new PromisedValue<>(value);
            unlock();
            if (successCallback != null)
                successCallback.onPromiseFulfilled(value);
        }
    }

    @Override
    public SimpleFuturePromise<T> setSuccessCallback(PromiseCallback<T> successCallback) {
        this.successCallback = successCallback;
        return this;
    }

    @Override
    public SimpleFuturePromise<T> setErrorCallback(PromiseCallback<Throwable> errorCallback) {
        this.errorCallback = errorCallback;
        return this;
    }

    @Override
    public void setTimeout(long timeOutInMillis) {
        this.timeOut = timeOutInMillis;
    }

    @Override
    public void error(Throwable error) {
        if (!isFulfilled()) {
            this.error = error;
            promisedValue = new PromisedValue<>(null);
            unlock();
            if (errorCallback != null)
                errorCallback.onPromiseFulfilled(error);
        }
    }

    @Override
    public Throwable getException() {
        return error;
    }
}

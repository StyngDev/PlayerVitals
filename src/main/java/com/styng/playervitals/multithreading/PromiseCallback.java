package com.styng.playervitals.multithreading;

public interface PromiseCallback<T> {

    void onPromiseFulfilled(T value);

}

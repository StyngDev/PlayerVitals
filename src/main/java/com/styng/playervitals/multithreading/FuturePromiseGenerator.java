package com.styng.playervitals.multithreading;

public interface FuturePromiseGenerator {

    default <T> FuturePromise<T> generateNew(Class<T> cls) {
        return generateNew();
    }

    <T> FuturePromise<T> generateNew();

}

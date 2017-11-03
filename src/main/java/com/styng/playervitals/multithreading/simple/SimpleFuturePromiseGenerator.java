package com.styng.playervitals.multithreading.simple;

import com.styng.playervitals.multithreading.FuturePromiseGenerator;
import com.styng.playervitals.multithreading.FuturePromise;

public class SimpleFuturePromiseGenerator implements FuturePromiseGenerator {

    @Override
    public <T> FuturePromise<T> generateNew() {
        return new SimpleFuturePromise<>();
    }
}

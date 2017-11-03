package com.styng.playervitals.multithreading;

public class Promise {

    private static FuturePromiseGenerator generator;

    public static FuturePromiseGenerator generator() {
        return generator;
    }

    public static void setGenerator(FuturePromiseGenerator generator) {
        Promise.generator = generator;
    }

    private Promise() {
    }

}

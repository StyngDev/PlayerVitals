package com.styng.playervitals.multithreading;

public class PromisedValue<T> {

    private final T value;

    public PromisedValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

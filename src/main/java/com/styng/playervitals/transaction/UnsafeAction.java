package com.styng.playervitals.transaction;

@FunctionalInterface
public interface UnsafeAction {

    void perform() throws Exception;

}

package com.styng.playervitals.effect;

public interface Effect<T extends Effectable> {

    void onStart(T target);

    void onEffectTick(T target);

    void onStop(T target);

    boolean shouldStop(T target);

    int getIterationDelay();

}

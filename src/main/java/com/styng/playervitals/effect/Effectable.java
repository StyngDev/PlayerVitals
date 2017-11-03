package com.styng.playervitals.effect;

public interface Effectable<T extends Effectable> {

    void startEffect(Effect<T> effect);

    void stopEffect(Class<? extends Effect> effectClass);

    boolean hasEffect(Class<? extends Effect> effectClass);

    void clearEffects();

}

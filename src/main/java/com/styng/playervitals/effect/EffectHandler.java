package com.styng.playervitals.effect;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class EffectHandler<T extends Effectable> {

    private final T target;
    private final Effect<T> effect;
    private BukkitTask task;
    private final Collection<Consumer<Effect<T>>> stopHandlers = new ArrayList<>();

    public EffectHandler(T target, Effect<T> effect) {
        this.target = target;
        this.effect = effect;
    }

    public T getTarget() {
        return target;
    }

    public Effect<T> getEffect() {
        return effect;
    }

    public void start(JavaPlugin plugin) {
        effect.onStart(target);
        this.task = new BukkitRunnable() {

            @Override
            public void run() {
                if (!effect.shouldStop(target)) {
                    effect.onEffectTick(target);
                } else {
                    stop();
                    stopHandlers.forEach(handler -> handler.accept(effect));
                }
            }

        }.runTaskTimer(plugin, 0, effect.getIterationDelay());
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            effect.onStop(target);
        }
    }

    public void onNaturalStop(Consumer<Effect<T>> handler) {
        stopHandlers.add(handler);
    }

}

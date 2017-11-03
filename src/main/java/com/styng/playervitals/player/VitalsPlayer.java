package com.styng.playervitals.player;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.effect.Effect;
import com.styng.playervitals.effect.EffectHandler;
import com.styng.playervitals.effect.Effectable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VitalsPlayer implements Effectable<VitalsPlayer> {

    private final UUID playerId;
    private Map<Class<? extends Effect>, EffectHandler<VitalsPlayer>> activeEffects = new HashMap<>();
    private LocalDateTime lastSlept;
    private LocalDateTime lastLoggedOut;
    private long lastSaved;
    private long timeNotSlept;

    public VitalsPlayer(UUID playerId) {
        this.playerId = playerId;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    public boolean isOnline() {
        return getBukkitPlayer() != null;
    }

    @Override
    public void startEffect(Effect<VitalsPlayer> effect) {
        stopEffect(effect.getClass());
        EffectHandler<VitalsPlayer> effectHandler = new EffectHandler<>(this, effect);
        effectHandler.onNaturalStop(vitalsPlayerEffect -> activeEffects.remove(vitalsPlayerEffect.getClass()));
        activeEffects.put(effect.getClass(), effectHandler);
    }

    @Override
    public void stopEffect(Class<? extends Effect> effectClass) {
        if (hasEffect(effectClass)) {
            activeEffects.get(effectClass).stop();
            activeEffects.remove(effectClass);
        }
    }

    @Override
    public boolean hasEffect(Class<? extends Effect> effectClass) {
        return activeEffects.containsKey(effectClass);
    }

    @Override
    public void clearEffects() {
        activeEffects.values().forEach(EffectHandler::stop);
        activeEffects.clear();
    }

    public LocalDateTime getLastSlept() {
        return lastSlept;
    }

    public void setLastSlept(LocalDateTime lastSlept, boolean update) {
        this.lastSlept = lastSlept;
        if (update) {
            PlayerVitals.getInstance().getPlayerStorage().getSleepStorage().setLastSlept(playerId, lastSlept);
        }
    }

    public LocalDateTime getLastLoggedOut() {
        return lastLoggedOut;
    }

    public void setLastLoggedOut(LocalDateTime lastLoggedOut, boolean update) {
        this.lastLoggedOut = lastLoggedOut;
        if (update) {
            PlayerVitals.getInstance().getPlayerStorage().setLastLoggedOut(playerId, lastLoggedOut);
        }
    }

    public void setTimeNotSlept(long timeNotSlept, boolean update) {
        this.timeNotSlept = timeNotSlept;
        this.lastSaved = System.currentTimeMillis();
        if (update)
            PlayerVitals.getInstance().getPlayerStorage().getSleepStorage().setTimeNotSlept(playerId, this.timeNotSlept);
    }

    public long getTimeNotSlept() {
        return timeNotSlept;
    }

    public long calculateTotalTimeNotSlept() {
        return getTimeNotSlept() + (System.currentTimeMillis() - Math.max(lastSaved, getTimestamp(getLastLoggedOut())));

    }

    private long getTimestamp(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}

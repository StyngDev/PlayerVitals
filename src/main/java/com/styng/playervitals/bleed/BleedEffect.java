package com.styng.playervitals.bleed;

import com.styng.playervitals.effect.Effect;
import com.styng.playervitals.player.VitalsPlayer;
import org.bukkit.entity.Player;

public class BleedEffect implements Effect<VitalsPlayer> {

    private final BleedSettings settings;

    public BleedEffect(BleedSettings settings) {
        this.settings = settings;
    }

    @Override
    public void onStart(VitalsPlayer target) {

    }

    @Override
    public void onEffectTick(VitalsPlayer target) {
        Player player = target.getBukkitPlayer();
        if (player != null) {
            player.damage(settings.getDamage());
        }
    }

    @Override
    public void onStop(VitalsPlayer target) {
    }

    @Override
    public boolean shouldStop(VitalsPlayer target) {
        return false;
    }

    @Override
    public int getIterationDelay() {
        return settings.getTicks();
    }
}

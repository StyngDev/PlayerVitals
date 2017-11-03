package com.styng.playervitals.sleep.actions;

import com.styng.playervitals.sleep.SleepAction;
import com.styng.playervitals.sleep.SleepSettings;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SlowAction implements SleepAction {

    private final long interval;

    public SlowAction(long interval) {
        this.interval = interval;
    }

    @Override
    public String getActionName() {
        return "slow";
    }

    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public void activate(Player player, SleepSettings settings) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, settings.getSlowLevel() - 1, false, false));
    }
}

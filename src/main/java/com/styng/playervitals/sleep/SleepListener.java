package com.styng.playervitals.sleep;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.player.VitalsPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.time.LocalDateTime;

public class SleepListener implements Listener {

    private final PlayerVitals plugin;
    private final SleepSettings settings;

    public SleepListener(PlayerVitals plugin, SleepSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent evt) {
        VitalsPlayer player = plugin.getVitalsPlayers().getLoaded(evt.getPlayer());
        player.setLastSlept(LocalDateTime.now(), true);
        player.setTimeNotSlept(0, true);
    }
}

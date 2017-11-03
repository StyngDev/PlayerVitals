package com.styng.playervitals.player;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.multithreading.FuturePromise;
import com.styng.playervitals.multithreading.Promise;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.time.LocalDateTime;

public class VitalsPlayerListeners implements Listener {

    private final PlayerVitals plugin;

    public VitalsPlayerListeners(PlayerVitals plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent evt) {
        if (evt.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            FuturePromise<VitalsPlayer> promise = Promise.generator().generateNew();
            plugin.getVitalsPlayers().loadPlayer(evt.getUniqueId(), promise);
            promise.join();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent evt) {
        VitalsPlayer player = plugin.getVitalsPlayers().getLoaded(evt.getPlayer());
        player.setLastLoggedOut(LocalDateTime.now(), true);
        player.setTimeNotSlept(player.calculateTotalTimeNotSlept(), true);
        plugin.getVitalsPlayers().unload(evt.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent evt) {
        plugin.getVitalsPlayers().getLoaded(evt.getEntity()).clearEffects();
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent evt) {
        savePlayerData();
    }

    private void savePlayerData() {
        plugin.getVitalsPlayers().getAllLoaded().forEach(player -> {
            player.setTimeNotSlept(player.calculateTotalTimeNotSlept(), true);
        });
    }
}

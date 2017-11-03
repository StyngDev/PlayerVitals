package com.styng.playervitals.player;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.multithreading.FuturePromise;
import com.styng.playervitals.multithreading.Promise;
import com.styng.playervitals.transaction.TransactionPool;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VitalsPlayers {

    private final PlayerVitals plugin;
    private final Map<UUID, VitalsPlayer> loadedPlayers = new HashMap<>();

    public VitalsPlayers(PlayerVitals plugin) {
        this.plugin = plugin;
    }

    public void loadPlayer(UUID playerId, FuturePromise<VitalsPlayer> resultCallback) {
        plugin.getPlayerStorage().hasStoredData(playerId, Promise.generator().generateNew(Boolean.class).setSuccessCallback(value -> {
            if (value) {
                TransactionPool.getInstance().runUnsafeTask(() -> {
                    VitalsPlayer player = new VitalsPlayer(playerId);

                    FuturePromise<LocalDateTime> lastLoggedOutResult = Promise.generator().generateNew();
                    FuturePromise<LocalDateTime> lastSleptResult = Promise.generator().generateNew();
                    FuturePromise<Long> timeNotSleptResult = Promise.generator().generateNew();

                    plugin.getPlayerStorage().getLastLoggedOut(playerId, lastLoggedOutResult);
                    plugin.getPlayerStorage().getSleepStorage().getLastSlept(playerId, lastSleptResult);
                    plugin.getPlayerStorage().getSleepStorage().getTimeNotSlept(playerId, timeNotSleptResult);

                    player.setLastLoggedOut(lastLoggedOutResult.value(), false);
                    player.setLastSlept(lastSleptResult.value(), false);
                    player.setTimeNotSlept(timeNotSleptResult.value(), false);

                    loadedPlayers.put(playerId, player);
                    if (resultCallback != null)
                        resultCallback.success(player);
                });
            } else {
                plugin.getPlayerStorage().initializePlayerData(playerId);
                VitalsPlayer player = new VitalsPlayer(playerId);
                player.setTimeNotSlept(0, false);
                loadedPlayers.put(playerId, player);
                if (resultCallback != null)
                    resultCallback.success(player);
            }
        }).setErrorCallback(value -> {
            if (resultCallback != null)
                resultCallback.error(value);
        }));
    }

    public boolean hasLoaded(Player player) {
        return hasLoaded(player.getUniqueId());
    }

    public boolean hasLoaded(UUID playerId) {
        return loadedPlayers.containsKey(playerId);
    }

    public VitalsPlayer getLoaded(Player player) {
        return getLoaded(player.getUniqueId());
    }

    public VitalsPlayer getLoaded(UUID playerId) {
        return loadedPlayers.get(playerId);
    }

    public Collection<VitalsPlayer> getAllLoaded() {
        return loadedPlayers.values();
    }

    public void unload(Player player) {
        unload(player.getUniqueId());
    }

    public void unload(UUID playerId) {
        if (hasLoaded(playerId)) {
            getLoaded(playerId).clearEffects();
            loadedPlayers.remove(playerId);
        }
    }
}
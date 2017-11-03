package com.styng.playervitals.storage.yaml;

import com.styng.playervitals.PlayerVitals;

import java.io.File;
import java.util.UUID;

public class YamlStorageHelper {

    public static File getPlayerFile(PlayerVitals plugin, UUID playerId) {
        return new File(plugin.getDataFolder(), "storage/" + playerId + ".yml");
    }

    private YamlStorageHelper() {
    }

}

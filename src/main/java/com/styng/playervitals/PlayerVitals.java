package com.styng.playervitals;

import com.styng.playervitals.bleed.Bandage;
import com.styng.playervitals.bleed.BleedListeners;
import com.styng.playervitals.bleed.BleedSettings;
import com.styng.playervitals.configuration.yaml.YamlConfigCache;
import com.styng.playervitals.configuration.yaml.serializers.YamlLocalDateTime;
import com.styng.playervitals.multithreading.Promise;
import com.styng.playervitals.multithreading.simple.SimpleFuturePromiseGenerator;
import com.styng.playervitals.player.VitalsPlayerListeners;
import com.styng.playervitals.player.VitalsPlayers;
import com.styng.playervitals.sleep.SleepListener;
import com.styng.playervitals.sleep.SleepSettings;
import com.styng.playervitals.storage.PlayerStorage;
import com.styng.playervitals.storage.yaml.YamlPlayerStorage;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerVitals extends JavaPlugin {

    private static PlayerVitals instance;

    public static PlayerVitals getInstance() {
        return instance;
    }

    private VitalsPlayers vitalsPlayers;
    private PlayerStorage playerStorage;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        YamlConfigCache.setDefaultCache(YamlConfigCache.withMaxCache(getConfig().getInt("max-config-cache-size", 150)));
        Promise.setGenerator(new SimpleFuturePromiseGenerator());

        this.vitalsPlayers = new VitalsPlayers(this);
        this.playerStorage = new YamlPlayerStorage(this);
        registerYamlSerializers();
        registerListeners();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        BleedSettings bleedSettings = BleedSettings.fromConfiguration(getConfig());
        bleedSettings.setBandage(Bandage.fromConfiguration(getConfig()));
        pluginManager.registerEvents(new BleedListeners(this, bleedSettings), this);

        pluginManager.registerEvents(new SleepListener(this, SleepSettings.fromConfiguration(getConfig())), this);
        pluginManager.registerEvents(new VitalsPlayerListeners(this), this);
    }

    private void registerYamlSerializers() {
        ConfigurationSerialization.registerClass(YamlLocalDateTime.class, "LocalDateTime");
    }

    public VitalsPlayers getVitalsPlayers() {
        return vitalsPlayers;
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }
}
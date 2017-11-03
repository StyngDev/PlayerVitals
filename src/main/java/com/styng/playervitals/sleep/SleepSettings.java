package com.styng.playervitals.sleep;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class SleepSettings {

    public static final int DEFAULT_SLOW_LEVEL = 1;

    public static SleepSettings fromConfiguration(FileConfiguration configuration) {
        return configuration.isSet("sleep") && configuration.isConfigurationSection("sleep") ?
                fromConfigurationSection(configuration.getConfigurationSection("sleep")) : new SleepSettings();
    }

    public static SleepSettings fromConfigurationSection(ConfigurationSection section) {
        return new SleepSettings(section);
    }

    private final int slowLevel;

    private SleepSettings() {
        this.slowLevel = DEFAULT_SLOW_LEVEL;
    }

    private SleepSettings(ConfigurationSection section) {
        this.slowLevel = section.getInt("slow_level", DEFAULT_SLOW_LEVEL);
    }

    public int getSlowLevel() {
        return slowLevel;
    }
}

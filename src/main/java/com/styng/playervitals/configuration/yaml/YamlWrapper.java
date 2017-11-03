package com.styng.playervitals.configuration.yaml;

import com.styng.playervitals.collections.LRUCache;
import com.styng.playervitals.configuration.ConfigurationWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlWrapper implements ConfigurationWrapper<FileConfiguration> {

    private final File file;
    private LRUCache.CacheEntry<String, FileConfiguration> configuration;

    public YamlWrapper(File file) {
        this.file = file;
    }

    @Override
    public void load() throws Exception {
        configuration = YamlConfigCache.getDefaultCache().getOrCreate(file, () -> YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public void reload() throws Exception {
        YamlConfigCache.getDefaultCache().update(file, YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public void save() throws Exception {
        configuration.getValue().save(file);
    }

    @Override
    public FileConfiguration getConfiguration() {
        return configuration.getValue();
    }
}

package com.styng.playervitals.configuration.yaml;

import com.styng.playervitals.collections.LRUCache;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.function.Supplier;

public class YamlConfigCache {

    private static YamlConfigCache defaultCache;

    public static synchronized void setDefaultCache(YamlConfigCache defaultCache) {
        YamlConfigCache.defaultCache = defaultCache;
    }

    public static YamlConfigCache getDefaultCache() {
        return defaultCache;
    }

    public static YamlConfigCache withMaxCache(int maxCache) {
        return new YamlConfigCache(maxCache);
    }

    private final LRUCache<String, FileConfiguration> configCache;

    private YamlConfigCache(int maxCache) {
        configCache = new LRUCache<>(maxCache);
    }

    public LRUCache.CacheEntry<String, FileConfiguration> getOrCreate(File file, Supplier<FileConfiguration> supplier) {
        return getOrCreate(file.getAbsolutePath(), supplier);
    }

    public LRUCache.CacheEntry<String, FileConfiguration> getOrCreate(String path, Supplier<FileConfiguration> supplier) {
        path = path.toLowerCase();
        LRUCache.CacheEntry<String, FileConfiguration> configuration = configCache.get(path);
        if (configuration == null)
            configuration = configCache.cache(path, supplier.get());
        return configuration;
    }

    public FileConfiguration update(File file, FileConfiguration configuration) {
        return update(file.getAbsolutePath(), configuration);
    }

    public FileConfiguration update(String path, FileConfiguration configuration) {
        configCache.cache(path.toLowerCase(), configuration);
        return configuration;
    }
}

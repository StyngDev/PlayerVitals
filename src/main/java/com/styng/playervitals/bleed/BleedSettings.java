package com.styng.playervitals.bleed;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class BleedSettings {

    private static final double DEFAULT_CHANCE = 0.5;
    private static final double DEFAULT_DAMAGE = 1d;
    private static final int DEFAULT_TICKS = 20;

    public static BleedSettings fromConfiguration(FileConfiguration configuration) {
        return configuration.isSet("bleed") && configuration.isConfigurationSection("bleed") ?
                fromConfigurationSection(configuration.getConfigurationSection("bleed")) :
                fromDamageAndTicksAndChance(DEFAULT_DAMAGE, DEFAULT_TICKS, DEFAULT_CHANCE);
    }

    public static BleedSettings fromConfigurationSection(ConfigurationSection section) {
        return fromDamageAndTicksAndChance(section.getDouble("damage", DEFAULT_DAMAGE), section.getInt("ticks", DEFAULT_TICKS),
                section.getDouble("chance", DEFAULT_CHANCE));
    }

    public static BleedSettings fromDamageAndTicksAndChance(double damage, int ticks, double chance) {
        return new BleedSettings(damage, ticks, chance);
    }

    private final double damage;
    private final int ticks;
    private final double chance;
    private Bandage bandage = Bandage.DEFAULT;

    private BleedSettings(double damage, int ticks, double chance) {
        this.damage = damage;
        this.ticks = ticks;
        this.chance = chance;
    }

    public double getDamage() {
        return damage;
    }

    public int getTicks() {
        return ticks;
    }

    public double getChance() {
        return chance;
    }

    public Bandage getBandage() {
        return bandage;
    }

    public void setBandage(Bandage bandage) {
        Preconditions.checkNotNull(bandage, "The bandage can't be null!");
        this.bandage = bandage;
    }
}

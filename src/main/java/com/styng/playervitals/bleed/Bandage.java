package com.styng.playervitals.bleed;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Bandage {

    public static final Bandage DEFAULT = Bandage.builder(Material.PAPER).displayName(ChatColor.GREEN + "Bandage")
            .addLines(ChatColor.GRAY + "Right click to heal", ChatColor.GRAY + "yourself from bleeding.").build();

    public static Bandage fromConfiguration(FileConfiguration configuration) {
        return configuration.isSet("bleed.bandage") && configuration.isConfigurationSection("bleed.bandage") ?
                fromConfigurationSection(configuration.getConfigurationSection("bleed.bandage")) : DEFAULT;
    }

    public static Bandage fromConfigurationSection(ConfigurationSection section) {
        return Bandage.builder(section.getInt("item_id", 339)).data((byte) section.getInt("data", 0))
                .displayName(ChatColor.translateAlternateColorCodes('&', section.getString("display_name", "&aBandage"))).addLines(
                        section.isSet("lore") && section.isList("lore") ? fixColors(section.getStringList("lore")) :
                                fixColors(Arrays.asList("&7Right click to heal yourself", "&7from the bleeding effect."))).build();
    }

    private static Collection<String> fixColors(Collection<String> collection) {
        return collection.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
    }

    public static BandageBuilder builder(int materialId) {
        return new BandageBuilder(materialId);
    }

    public static BandageBuilder builder(Material material) {
        return new BandageBuilder(material);
    }

    public static class BandageBuilder {

        private final Material material;
        private byte data;
        private String displayName;
        private final Collection<String> lore = new ArrayList<>();

        private BandageBuilder(int materialId) {
            this(Material.getMaterial(materialId));
        }

        private BandageBuilder(Material material) {
            this.material = material;
        }

        public BandageBuilder data(byte data) {
            this.data = data;
            return this;
        }

        public BandageBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public BandageBuilder addLines(String... lines) {
            return addLines(Arrays.asList(lines));
        }

        public BandageBuilder addLines(Collection<String> lines) {
            this.lore.addAll(lines);
            return this;
        }

        public Bandage build() {
            return new Bandage(this);
        }
    }

    private final ItemStack itemStack;

    private Bandage(BandageBuilder builder) {
        this.itemStack = new ItemStack(builder.material, 1, builder.data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (builder.displayName != null)
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', builder.displayName));
        itemMeta.setLore(builder.lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).collect(Collectors.toList()));
        this.itemStack.setItemMeta(itemMeta);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public boolean isBandage(ItemStack itemStack) {
        return itemStack.isSimilar(this.itemStack);
    }
}

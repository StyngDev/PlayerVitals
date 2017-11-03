package com.styng.playervitals.bleed;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.player.VitalsPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BleedListeners implements Listener {

    private final PlayerVitals plugin;
    private final BleedSettings settings;

    public BleedListeners(PlayerVitals plugin, BleedSettings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler
    public void onPlayerDamagedByEntity(EntityDamageByEntityEvent evt) {
        if (evt.getEntity() instanceof Player) {
            VitalsPlayer player = plugin.getVitalsPlayers().getLoaded((Player) evt.getEntity());
            if (!player.hasEffect(BleedEffect.class) && Math.random() <= settings.getChance()) {
                player.startEffect(new BleedEffect(settings));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent evt) {
        if (evt.getAction() != Action.PHYSICAL && evt.getItem() != null && settings.getBandage().isBandage(evt.getItem())) {
            VitalsPlayer player = plugin.getVitalsPlayers().getLoaded(evt.getPlayer());
            if (player.hasEffect(BleedEffect.class)) {
                player.stopEffect(BleedEffect.class);
                player.getBukkitPlayer().sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "You healed your bleeding!");
                if (evt.getItem().getAmount() > 1) {
                    evt.getItem().setAmount(evt.getItem().getAmount() - 1);
                } else {
                    player.getBukkitPlayer().setItemInHand(null);
                }
            } else {
                player.getBukkitPlayer().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "You are not bleeding!");
            }
        }
    }
}
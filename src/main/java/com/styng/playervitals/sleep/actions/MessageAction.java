package com.styng.playervitals.sleep.actions;

import com.styng.playervitals.sleep.SleepAction;
import com.styng.playervitals.sleep.SleepSettings;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MessageAction implements SleepAction {

    private final long interval;
    private final String message;

    public MessageAction(long interval, ConfigurationSection section) {
        if (!section.isSet("message") || !section.isString("message"))
            throw new IllegalArgumentException("The given configuration section either doesn't have a defined message or it isn't a string");
        this.interval = interval;
        this.message = ChatColor.translateAlternateColorCodes('&', section.getString("message"));
    }

    @Override
    public String getActionName() {
        return "message";
    }

    @Override
    public long getInterval() {
        return interval;
    }

    @Override
    public void activate(Player player, SleepSettings settings) {
        player.sendMessage(message);
    }
}

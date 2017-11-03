package com.styng.playervitals.sleep;

import com.styng.playervitals.sleep.actions.MessageAction;
import com.styng.playervitals.sleep.actions.SlowAction;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public interface SleepAction {

    static Collection<SleepAction> create(ConfigurationSection section, long interval) {
        Collection<SleepAction> actions = new ArrayList<>();
        if (section.isSet("actions")) {
            Collection<String> actionNames = section.isList("actions") ? section.getStringList("actions") :
                    Collections.singletonList(section.getString("actions"));
            actionNames.forEach(name -> {
                SleepAction action = create(name, interval, section);
                if (action != null)
                    actions.add(action);
            });
        }
        return actions;
    }

    static SleepAction create(String action, long interval, ConfigurationSection section) {
        action = action.toLowerCase();
        switch (action) {
            case "message":
                return new MessageAction(interval, section);
            case "slow":
                return new SlowAction(interval);
        }
        return null;
    }

    String getActionName();

    long getInterval();

    void activate(Player player, SleepSettings settings);

}

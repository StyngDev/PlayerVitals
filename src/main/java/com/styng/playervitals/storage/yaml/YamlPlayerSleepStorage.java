package com.styng.playervitals.storage.yaml;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.configuration.yaml.YamlWrapper;
import com.styng.playervitals.configuration.yaml.serializers.YamlLocalDateTime;
import com.styng.playervitals.multithreading.FuturePromise;
import com.styng.playervitals.storage.PlayerSleepStorage;
import com.styng.playervitals.transaction.Transaction;
import com.styng.playervitals.transaction.TransactionPool;
import com.styng.playervitals.transaction.yaml.YamlSetAction;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.styng.playervitals.storage.yaml.YamlStorageHelper.getPlayerFile;

public class YamlPlayerSleepStorage implements PlayerSleepStorage {

    private final PlayerVitals plugin;

    public YamlPlayerSleepStorage(PlayerVitals plugin) {
        this.plugin = plugin;
    }

    @Override
    public void getLastSlept(UUID playerId, FuturePromise<LocalDateTime> resultPromise) {
        TransactionPool.getInstance().runUnsafeTask(() -> {
            YamlWrapper wrapper = new YamlWrapper(YamlStorageHelper.getPlayerFile(plugin, playerId));
            wrapper.load();
            if (resultPromise != null)
                resultPromise.success(wrapper.getConfiguration().isSet("sleep.last_slept") ?
                        ((YamlLocalDateTime) wrapper.getConfiguration().get("sleep.last_slept")).getLocalDateTime() : null);
        });
    }

    @Override
    public void setLastSlept(UUID playerId, LocalDateTime time) {
        Transaction transaction = new Transaction();
        transaction
                .add(new YamlSetAction(YamlStorageHelper.getPlayerFile(plugin, playerId), "sleep.last_slept").setValue(new YamlLocalDateTime(time)));
        TransactionPool.getInstance().commitTransaction(transaction);
    }

    @Override
    public void getTimeNotSlept(UUID playerId, FuturePromise<Long> resultPromise) {
        TransactionPool.getInstance().runUnsafeTask(() -> {
            YamlWrapper wrapper = new YamlWrapper(YamlStorageHelper.getPlayerFile(plugin, playerId));
            wrapper.load();
            if (resultPromise != null)
                resultPromise.success(wrapper.getConfiguration().getLong("sleep.time_not_slept", 0L));
        });
    }

    @Override
    public void setTimeNotSlept(UUID playerId, long timeMillis) {
        Transaction transaction = new Transaction();
        transaction.add(new YamlSetAction(getPlayerFile(plugin, playerId), "sleep.last_slept").setValue(timeMillis));
        TransactionPool.getInstance().commitTransaction(transaction);
    }


}

package com.styng.playervitals.storage.yaml;

import com.styng.playervitals.PlayerVitals;
import com.styng.playervitals.configuration.yaml.YamlWrapper;
import com.styng.playervitals.configuration.yaml.serializers.YamlLocalDateTime;
import com.styng.playervitals.multithreading.FuturePromise;
import com.styng.playervitals.storage.PlayerSleepStorage;
import com.styng.playervitals.storage.PlayerStorage;
import com.styng.playervitals.transaction.Action;
import com.styng.playervitals.transaction.Transaction;
import com.styng.playervitals.transaction.TransactionException;
import com.styng.playervitals.transaction.TransactionPool;
import com.styng.playervitals.transaction.yaml.YamlSetAction;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.styng.playervitals.storage.yaml.YamlStorageHelper.getPlayerFile;

public class YamlPlayerStorage implements PlayerStorage {

    private final PlayerVitals plugin;
    private final YamlPlayerSleepStorage sleepStorage;

    public YamlPlayerStorage(PlayerVitals plugin) {
        this.plugin = plugin;
        this.sleepStorage = new YamlPlayerSleepStorage(plugin);
    }

    @Override
    public void hasStoredData(UUID playerId, FuturePromise<Boolean> resultPromise) {
        TransactionPool.getInstance().runUnsafeTask(() -> {
            File file = getPlayerFile(plugin, playerId);
            resultPromise.success(file.exists());
        });
    }

    @Override
    public void initializePlayerData(UUID playerId) {
        File file = getPlayerFile(plugin, playerId);
        Transaction transaction = new Transaction();
        transaction.add(new Action() {

            boolean rollbackParentDirectory;
            boolean rollbackFile;

            @Override
            public void execute() throws Exception {
                if (!file.getParentFile().exists()) {
                    if (file.getParentFile().mkdirs())
                        rollbackParentDirectory = true;
                    else
                        throw new TransactionException("Couldn't create parent directory!");
                }

                if (!file.exists()) {
                    if (file.createNewFile())
                        rollbackFile = true;
                    else
                        throw new TransactionException("Couldn't create player file!");
                }
            }

            @Override
            public void rollback() {
                if (rollbackFile)
                    file.delete();
                if (rollbackParentDirectory)
                    file.getParentFile().delete();
            }
        });
        TransactionPool.getInstance().commitTransaction(transaction);
    }

    @Override
    public PlayerSleepStorage getSleepStorage() {
        return sleepStorage;
    }

    @Override
    public void getLastLoggedOut(UUID playerId, FuturePromise<LocalDateTime> resultPromise) {
        TransactionPool.getInstance().runUnsafeTask(() -> {
            YamlWrapper wrapper = new YamlWrapper(YamlStorageHelper.getPlayerFile(plugin, playerId));
            wrapper.load();
            LocalDateTime time = wrapper.getConfiguration().isSet("lastLoggedOut") ?
                    ((YamlLocalDateTime) wrapper.getConfiguration().get("lastLoggedOut")).getLocalDateTime() : null;
            if (resultPromise != null)
                resultPromise.success(time);
        });
    }

    @Override
    public void setLastLoggedOut(UUID playerId, LocalDateTime time) {
        Transaction transaction = new Transaction();
        transaction.add(new YamlSetAction(getPlayerFile(plugin, playerId), "lastLoggedOut").setValue(time));
        TransactionPool.getInstance().commitTransaction(transaction);
    }

    @Override
    public void getLastLoggedIn(UUID playerId, FuturePromise<LocalDateTime> resultPromise) {
        TransactionPool.getInstance().runUnsafeTask(() -> {
            YamlWrapper wrapper = new YamlWrapper(YamlStorageHelper.getPlayerFile(plugin, playerId));
            wrapper.load();
            LocalDateTime time = wrapper.getConfiguration().isSet("lastLoggedIn") ?
                    ((YamlLocalDateTime) wrapper.getConfiguration().get("lastLoggedIn")).getLocalDateTime() : null;
            if (resultPromise != null)
                resultPromise.success(time);
        });
    }

    @Override
    public void setLastLoggedIn(UUID playerId, LocalDateTime time) {
        Transaction transaction = new Transaction();
        transaction.add(new YamlSetAction(getPlayerFile(plugin, playerId), "lastLoggedIn").setValue(time));
        TransactionPool.getInstance().commitTransaction(transaction);
    }
}

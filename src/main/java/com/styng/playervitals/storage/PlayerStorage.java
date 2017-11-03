package com.styng.playervitals.storage;

import com.styng.playervitals.multithreading.FuturePromise;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PlayerStorage {

    void hasStoredData(UUID playerId, FuturePromise<Boolean> resultPromise);

    void initializePlayerData(UUID playerId);

    PlayerSleepStorage getSleepStorage();

    void getLastLoggedOut(UUID playerId, FuturePromise<LocalDateTime> resultPromise);

    void setLastLoggedOut(UUID playerId, LocalDateTime time);

    void getLastLoggedIn(UUID playerId, FuturePromise<LocalDateTime> resultPromise);

    void setLastLoggedIn(UUID playerId, LocalDateTime time);
}

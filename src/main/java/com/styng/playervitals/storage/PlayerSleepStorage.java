package com.styng.playervitals.storage;

import com.styng.playervitals.multithreading.FuturePromise;

import java.time.LocalDateTime;
import java.util.UUID;

public interface PlayerSleepStorage {

    void getLastSlept(UUID playerId, FuturePromise<LocalDateTime> resultPromise);

    void setLastSlept(UUID playerId, LocalDateTime time);

    void getTimeNotSlept(UUID playerId, FuturePromise<Long> resultPromise);

    void setTimeNotSlept(UUID playerId, long timeMillis);

}

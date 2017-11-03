package com.styng.playervitals.configuration.yaml.serializers;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@SerializableAs("LocalDateTime")
public class YamlLocalDateTime implements ConfigurationSerializable {

    public static LocalDateTime deserialize(Map<String, Object> serialized) {
        ZoneId timeZone = ZoneId.of((String) serialized.getOrDefault("timezone", ZoneId.systemDefault().getId()));
        long millis = (Long) serialized.getOrDefault("millis", 0L);
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), timeZone);
    }

    private final LocalDateTime localDateTime;

    public YamlLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        ZoneId zoneId = ZoneId.systemDefault();
        map.put("timezone", zoneId.getId());
        map.put("millis", localDateTime.atZone(zoneId).toInstant().toEpochMilli());
        return map;
    }


}

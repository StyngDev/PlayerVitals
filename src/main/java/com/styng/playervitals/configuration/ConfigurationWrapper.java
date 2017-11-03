package com.styng.playervitals.configuration;

public interface ConfigurationWrapper<T> {

    void load() throws Exception;

    void reload() throws Exception;

    void save() throws Exception;

    T getConfiguration();

}

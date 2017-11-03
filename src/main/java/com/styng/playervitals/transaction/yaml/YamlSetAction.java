package com.styng.playervitals.transaction.yaml;

import com.styng.playervitals.configuration.yaml.YamlWrapper;
import com.styng.playervitals.transaction.Action;

import java.io.File;

public class YamlSetAction implements Action {

    private final YamlWrapper config;
    private final String path;
    private Object value;
    private Object previousValue;

    public YamlSetAction(File file, String path) {
        this(new YamlWrapper(file), path);
    }

    public YamlSetAction(YamlWrapper config, String path) {
        this.config = config;
        this.path = path;
        previousValue = config.getConfiguration().get(path);
    }

    public YamlSetAction setValue(Object value) {
        this.value = value;
        return this;
    }

    @Override
    public void execute() throws Exception {
        if (config.getConfiguration() == null)
            config.load();
        config.getConfiguration().set(path, value);
        config.save();
    }

    @Override
    public void rollback() throws Exception {
        if (config.getConfiguration() != null) {
            config.getConfiguration().set(path, previousValue);
            config.save();
        }
    }
}

package me.xiaoying.moebroker.api.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamlConfiguration {
    private HashMap<Object, Object> properties = new HashMap<>();

    private Object get(String key) {
        String separator = ".";
        String[] separatorKeys;

        if (key.contains(separator))
            separatorKeys = key.split("\\.");
        else
            return this.properties.get(key);

        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) this.properties.get(separatorKeys[i]);
                continue;
            }

            if (finalValue == null)
                break;

            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }

    public String getString(String key) {
        return this.getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        Object object = this.get(key);

        if (object == null)
            return defaultValue;

        return object.toString();
    }

    public List<String> getStringList(String key) {
        return this.getStringList(key, null);
    }

    public List<String> getStringList(String key, List<String> defaultValue) {
        Object object = this.get(key);

        if (object == null)
            return defaultValue;

        if (!(object instanceof List))
            return defaultValue;

        return (List<String>) object;
    }

    public int getInt(String key) {
        return this.getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object object = this.get(key);

        if (object == null)
            return defaultValue;

        return Integer.parseInt(object.toString());
    }

    public boolean getBoolean(String key) {
        return this.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object object = this.get(key);

        if (object == null)
            return defaultValue;

        return Boolean.parseBoolean(object.toString());
    }

    public double getDouble(String key) {
        return this.getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        Object object = this.get(key);

        if (object == null)
            return defaultValue;

        return Double.parseDouble(object.toString());
    }

    public long getLong(String key) {
        return this.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        Object object = this.get(key);

        if (object == null)
            return defaultValue;

        return Long.parseLong(object.toString());
    }

    public void load(File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            this.properties = new Yaml().loadAs(in, HashMap.class);

            for (Object o : this.properties.keySet()) {
                Object value = null;

                if (o instanceof Long || o instanceof Integer)
                    value = this.properties.get(o);

                if (value == null)
                    continue;

                this.properties.remove(o);
                this.properties.put(o.toString(), value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load(String content) {
        Yaml yaml = new Yaml();
        this.properties = yaml.loadAs(content, HashMap.class);

        if (this.properties == null)
            return;

        for (Object o : this.properties.keySet()) {
            Object value = null;

            if (o instanceof Long || o instanceof Integer)
                value = this.properties.get(o);

            if (value == null)
                continue;

            this.properties.remove(o);
            this.properties.put(o.toString(), value);
        }
    }

    public static YamlConfiguration loadConfiguration(File file) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(file);
        return configuration;
    }

    public static YamlConfiguration loadConfiguration(String file) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(file);
        return configuration;
    }
}

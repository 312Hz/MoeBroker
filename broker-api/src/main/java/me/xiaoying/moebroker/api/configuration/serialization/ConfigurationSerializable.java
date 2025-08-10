package me.xiaoying.moebroker.api.configuration.serialization;

import java.util.Map;

public interface ConfigurationSerializable {
    Map<String, Object> serialize();
}
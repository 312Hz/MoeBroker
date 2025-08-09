package me.xiaoying.moebroker.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrokerAddress {
    private final String host;
    private final int port;

    public BrokerAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
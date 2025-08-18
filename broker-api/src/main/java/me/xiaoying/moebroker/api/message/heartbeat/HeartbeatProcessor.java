package me.xiaoying.moebroker.api.message.heartbeat;

import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.processor.SyncProcessor;

public class HeartbeatProcessor extends SyncProcessor<HeartbeatPingMessage> {
    @Override
    public Object syncRequest(RemoteClient remote, HeartbeatPingMessage heartbeatPingMessage) throws Exception {
        return new HeartbeatPongMessage();
    }
}
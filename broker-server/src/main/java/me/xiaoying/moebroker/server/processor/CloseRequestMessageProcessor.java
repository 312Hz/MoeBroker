package me.xiaoying.moebroker.server.processor;

import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.message.close.CloseRequestMessage;
import me.xiaoying.moebroker.api.message.close.CloseResponseMessage;
import me.xiaoying.moebroker.api.processor.SyncProcessor;

public class CloseRequestMessageProcessor extends SyncProcessor<CloseRequestMessage> {
    @Override
    public Object syncRequest(RemoteClient remote, CloseRequestMessage closeRequestMessage) throws Exception {
        remote.getChannel().close();
        return new CloseResponseMessage(true);
    }
}
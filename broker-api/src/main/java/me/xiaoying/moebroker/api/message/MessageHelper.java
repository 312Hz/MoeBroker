package me.xiaoying.moebroker.api.message;

import io.netty.channel.Channel;
import me.xiaoying.moebroker.api.executor.ExecutorManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageHelper {
    private static final Map<String, Message> messages = new HashMap<>();

    static {
        // 一般情况只会有一个线程运行，所以直接指定大小为 1
        // 移除过期消息
        ExecutorManager.getFixedExecutor("message_helper", 1).execute(() -> {
            Iterator<Map.Entry<String, Message>> iterator = MessageHelper.messages.entrySet().iterator();

            Map.Entry<String, Message> entry;
            while (iterator.hasNext() && (entry = iterator.next()) != null) {
                if (entry.getValue().alive())
                    continue;

                iterator.remove();
            }
        });
    }

    public static Message getMessage(String id) {
        return MessageHelper.messages.get(id);
    }

    /**
     * 捕捉消息
     *
     * @param message Message
     */
    public static void captureMessage(Message message, Channel channel) {
        message.setTimestamp(System.currentTimeMillis());

        if (message instanceof RequestMessage) {
            RequestMessage requestMessage = (RequestMessage) message;
            requestMessage.setChannel(channel);
        }

        MessageHelper.messages.put(message.getUuid(), message);
    }

    /**
     * 回复消息
     *
     * @param message ResponseMessage
     */
    public static void receiveMessage(ResponseMessage message) {
        // 消息过期或不存在此消息
        Message targetMessage;
        if ((targetMessage = MessageHelper.messages.get(message.getTarget())) == null)
            return;

        // 消息类型不匹配
        if (!(targetMessage instanceof RequestMessage))
            return;

        RequestMessage requestMessage = (RequestMessage) targetMessage;

        // 目标消息不需要返回内容
        if (!requestMessage.isNeedResponse())
            return;

        MessageHelper.captureMessage(message, ((RequestMessage) targetMessage).getChannel());
        requestMessage.getChannel().writeAndFlush(message);
    }
}
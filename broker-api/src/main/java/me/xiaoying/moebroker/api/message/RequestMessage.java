package me.xiaoying.moebroker.api.message;

import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.concurrent.CompletableFuture;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestMessage extends Message {
    private static final long serialVersionUID = -1413233191796242701L;

    /** 通讯连接(不被序列化传输，各端自行设置) */
    private transient CompletableFuture<Object> future;

    /** 通讯连接 */
    private transient Channel channel;

    /** 消息内容 */
    private Object object;

    /** 是否需要返回消息 */
    private boolean needResponse;

    public RequestMessage(Object object) {
        this.object = object;
    }
}
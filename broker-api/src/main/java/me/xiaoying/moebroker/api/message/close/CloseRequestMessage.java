package me.xiaoying.moebroker.api.message.close;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CloseRequestMessage implements Serializable {
    private static final long serialVersionUID = -4457405630905533289L;

    /** 关闭链接理由 */
    private final String reason;

    /** 时间戳 */
    private final long timestamp;

    public CloseRequestMessage(String reason, long timestamp) {
        this.reason = reason;
        this.timestamp = timestamp;
    }
}
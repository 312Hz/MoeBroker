package me.xiaoying.moebroker.api.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 5149138872586970216L;

    /** 消息ID */
    private final String uuid;

    /** 消息存活时长(用于 SyncProcessor 返回消息) */
    private long keepAlive = 5000;

    /** 消息记录时间(应该交给接收端记录) */
    private long timestamp;

    public Message() {
        // 交给 Message 自行生成，避免手动指定重复 UUID 导致消息传输出错
        this.uuid = UUID.randomUUID().toString();
    }

    /** 判断消息是否过期 */
    public boolean alive() {
        return System.currentTimeMillis() - this.timestamp < this.keepAlive;
    }
}
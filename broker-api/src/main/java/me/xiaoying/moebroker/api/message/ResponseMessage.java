package me.xiaoying.moebroker.api.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseMessage extends Message {
    private static final long serialVersionUID = 930488755343627615L;

    /** 目标消息 ID */
    private String target;

    /** 返回内容 */
    private Object object;
}
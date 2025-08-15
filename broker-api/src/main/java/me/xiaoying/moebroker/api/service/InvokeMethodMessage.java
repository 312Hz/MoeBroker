package me.xiaoying.moebroker.api.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvokeMethodMessage implements Serializable {
    private static final long serialVersionUID = -3601723796072840213L;

    /** Service 类 */
    private final Class<?> service;

    /** 方法名称 */
    private final String methodName;

    /**
     * 方法传递参数<br>
     * 参数必须支持序列化
     */
    private final Object[] args;

    public InvokeMethodMessage(Class<?> service, String methodName, Object[] args) {
        this.service = service;
        this.methodName = methodName;
        this.args = args;
    }
}
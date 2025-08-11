package me.xiaoying.moebroker.api.message;

import lombok.Getter;

public class ObjectMessage implements Message {
    private static final long serialVersionUID = -5426380206404091302L;

    @Getter
    public final Object object;

    public ObjectMessage(Object object) {
        this.object = object;
    }
}
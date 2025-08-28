package me.xiaoying.moebroker.api.message.close;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CloseResponseMessage implements Serializable {
    private static final long serialVersionUID = 3685080779496897870L;

    private final boolean accepted;

    public CloseResponseMessage(final boolean accepted) {
        this.accepted = accepted;
    }
}
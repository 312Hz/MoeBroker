package me.xiaoying.moebroker.server.bootstrap.api.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisteredListener {
    private final Listener listener;

    private final EventPriority priority;

    private final Method method;

    public RegisteredListener(final Listener listener, final EventPriority priority, final Method method) {
        this.listener = listener;
        this.priority = priority;
        this.method = method;
    }

    public void callEvent(Event event) {
        this.method.setAccessible(true);

        try {
            this.method.invoke(event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean useful(Event event) {
        return event.getClass() == this.method.getParameters()[0].getType();
    }
}
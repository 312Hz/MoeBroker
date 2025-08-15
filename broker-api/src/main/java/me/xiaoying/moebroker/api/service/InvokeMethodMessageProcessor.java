package me.xiaoying.moebroker.api.service;

import me.xiaoying.moebroker.api.RemoteClient;
import me.xiaoying.moebroker.api.processor.SyncProcessor;

import java.lang.reflect.Method;

public class InvokeMethodMessageProcessor extends SyncProcessor<InvokeMethodMessage> {
    @Override
    public Object syncRequest(RemoteClient remote, InvokeMethodMessage message) throws Exception {
        Object service = ServiceManager.getService(message.getService());

        Method method = null;

        for (Method declaredMethod : service.getClass().getDeclaredMethods()) {
            if (!declaredMethod.getName().equals(message.getMethodName()))
                continue;

            if (declaredMethod.getParameters().length != message.getArgs().length)
                continue;

            method = declaredMethod;
            break;
        }

        if (method == null)
            return null;

        Object invoke = method.invoke(service, message.getArgs());
        return invoke;
    }
}
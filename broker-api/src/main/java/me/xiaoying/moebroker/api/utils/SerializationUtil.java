package me.xiaoying.moebroker.api.utils;

import me.xiaoying.moebroker.api.Broker;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationUtil {
    private static final Map<String, Class<?>> knownClasses = new HashMap<>();

    public static byte[] serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    public static <T> T deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        }
    }

    public static <T> T deserialize(byte[] data, List<ClassLoader> classLoaders) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            ObjectInputStream ois = new ObjectInputStream(bis) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) {
                    Class<?> clazz;
                    if ((clazz = SerializationUtil.knownClasses.get(desc.getName())) != null)
                        return clazz;

                    for (ClassLoader classLoader : classLoaders) {
                        try {
                            clazz = classLoader.loadClass(desc.getName());

                            SerializationUtil.knownClasses.put(desc.getName(), clazz);
                        } catch (ClassNotFoundException e) {}
                    }

                    return clazz;
                }
            };
            return (T) ois.readObject();
        }
    }
}
package me.xiaoying.moebroker.server.bootstrap.api.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassUtil {
    /**
     * 加载 ClassLoader 中 class 数据
     *
     * @param name class 名称
     * @param classLoader 加载 class 的 ClassLoader
     * @return byte[]
     */
    public static byte[] getClassData(String name, ClassLoader classLoader) {
        String path = name.replace('.', '/') + ".class";

        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            if (inputStream == null)
                return null;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;

            byte[] data = new byte[4096];

            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1)
                byteArrayOutputStream.write(data, 0, bytesRead);

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
package me.xiaoying.moebroker.api.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPUtil {
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos)) {
            gzip.write(data);
        }
        return bos.toByteArray();
    }

    public static byte[] decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (GZIPInputStream gzip = new GZIPInputStream(bis)) {

            byte[] buffer = new byte[1024];
            int len;

            while ((len = gzip.read(buffer)) > 0)
                bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }

    public static byte[] compressString(String str) throws IOException {
        return compress(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String decompressToString(byte[] compressedData) throws IOException {
        return new String(decompress(compressedData), StandardCharsets.UTF_8);
    }

    public static void compressFile(String sourceFile, String compressedFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(compressedFile);
             GZIPOutputStream gzipOS = new GZIPOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            int len;

            while ((len = fis.read(buffer)) != -1)
                gzipOS.write(buffer, 0, len);
        }
    }

    public static void decompressFile(String compressedFile, String decompressedFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(compressedFile);
             GZIPInputStream gzipIS = new GZIPInputStream(fis);
             FileOutputStream fos = new FileOutputStream(decompressedFile)) {

            byte[] buffer = new byte[1024];
            int len;

            while ((len = gzipIS.read(buffer)) != -1)
                fos.write(buffer, 0, len);
        }
    }
}
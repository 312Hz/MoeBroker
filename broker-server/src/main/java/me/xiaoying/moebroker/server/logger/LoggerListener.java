package me.xiaoying.moebroker.server.logger;

import me.xiaoying.logger.ChatColor;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.log.LogEndEvent;
import me.xiaoying.logger.event.log.PrepareLogEvent;
import me.xiaoying.logger.utils.ColorUtil;
import me.xiaoying.logger.utils.DateUtil;
import me.xiaoying.moebroker.api.utils.GZIPUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoggerListener implements Listener {
    public LoggerListener() {
//        try {
//            FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/logs/latest.log");
//            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
//
//            for (StackTraceElement stackTraceElement : stackTraceElements) {
//                fileWriter.write(stackTraceElement.toString() + "\n");
//            }
//
//            fileWriter.write("----------------------\n");
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @EventHandler
    public void onPrepareLog(PrepareLogEvent event) {
        String dateFormat = event.getLogger().getDateFormat();

        if (dateFormat.equalsIgnoreCase("HH:mm:ss"))
            return;

        event.getLogger().setDateFormat("HH:mm:ss");
    }

    @EventHandler
    public void onLogEndEvent(LogEndEvent event) {
        try {
            FileWriter fileWriter = new FileWriter("./logs/latest.log", true);
            fileWriter.write(ChatColor.stripColor(event.getMessage()) + "\n");
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LoggerListener initialize() {
        File logs = new File("logs");

        if (!logs.exists()) {
            logs.mkdirs();
        } else if (!logs.isDirectory()) {
            logs.delete();
            logs.mkdirs();
        }

        File file = new File(logs, "latest.log");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        // 获取稳定的日期前缀（使用文件修改时间或当前时间）
        String prefix = DateUtil.getDate(new Date(), "yyyy-MM-dd");

        // 查找已存在的最大索引
        int maxIndex = 0;
        Pattern pattern = Pattern.compile(Pattern.quote(prefix) + "-(\\d+)\\.log\\.gz");

        for (File listFile : Objects.requireNonNull(logs.listFiles())) {
            Matcher matcher = pattern.matcher(listFile.getName());
            if (matcher.matches()) {
                int currentIndex = Integer.parseInt(matcher.group(1));
                if (currentIndex > maxIndex) {
                    maxIndex = currentIndex;
                }
            }
        }

        String name = prefix + "-" + (maxIndex + 1) + ".log.gz";
        File compressedFile = new File(logs, name);

        int retry = 0;
        while (compressedFile.exists() && retry++ < 10) {
            name = prefix + "-" + (maxIndex + 1 + retry) + ".log.gz";
            compressedFile = new File(logs, name);
        }

        try {
            GZIPUtil.compressFile(file.getAbsolutePath(), compressedFile.getAbsolutePath());
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }
}
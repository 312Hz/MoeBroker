package me.xiaoying.moebroker.server.logger;

import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.log.PrepareLogEvent;

public class LoggerListener implements Listener {
    @EventHandler
    public void onPrepareLog(PrepareLogEvent event) {
        String dateFormat = event.getLogger().getDateFormat();

        if (dateFormat.equalsIgnoreCase("HH:mm:ss"))
            return;

        event.getLogger().setDateFormat("HH:mm:ss");
    }
}
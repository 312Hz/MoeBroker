package me.xiaoying.moebroker.server.bootstrap;

import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.log.LogEndEvent;
import me.xiaoying.logger.event.log.PrepareLogEvent;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.executor.ExecutorManager;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Terminal implements Listener {
    private final String prompt = "> ";

    public Terminal() {
        ExecutorManager.getScheduledExecutor("terminal", 1);
    }

    @EventHandler
    public void onPrepareLogEvent(PrepareLogEvent event) {
        Broker.getLogger().print("\r                         \r");
    }

    @EventHandler
    public void onLogEndEvent(LogEndEvent event) {
        Broker.getLogger().print(this.prompt);
    }

    public void run() {
        ExecutorManager.getScheduledExecutor("terminal").scheduleAtFixedRate(() -> {
//            Broker.getLogger().print(this.prompt);

            Scanner scanner = new Scanner(System.in);
            System.out.println(scanner.nextLine());
        }, 1, 1, TimeUnit.NANOSECONDS);
    }
}
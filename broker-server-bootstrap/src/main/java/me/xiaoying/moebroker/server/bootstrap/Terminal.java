package me.xiaoying.moebroker.server.bootstrap;

import me.xiaoying.logger.event.EventHandle;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.log.LogEndEvent;
import me.xiaoying.logger.event.log.PrepareLogEvent;
import me.xiaoying.moebroker.api.Broker;
import me.xiaoying.moebroker.api.executor.ExecutorManager;
import me.xiaoying.moebroker.server.bootstrap.api.BCore;
import me.xiaoying.moebroker.server.bootstrap.api.command.Command;
import me.xiaoying.moebroker.server.bootstrap.command.ConsoleSender;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Terminal implements Listener {
    private ScheduledFuture<?> scheduledFuture;

    private final String prompt = "> ";

    @EventHandler
    public void onPrepareLog(PrepareLogEvent event) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < this.prompt.length(); i++)
            stringBuilder.append(" ");

        Broker.getLogger().print("\r" + stringBuilder + "\r");
    }

    @EventHandler
    public void onLogEnd(LogEndEvent event) {
        Broker.getLogger().print(this.prompt);
    }

    public Terminal() {
        ExecutorManager.getScheduledExecutor("terminal", 1);
    }

    public void run() {
        LineReader lineReader;

        try {
            lineReader = LineReaderBuilder.builder()
                    .terminal(TerminalBuilder.builder().system(true).build())
                    .completer(this::advancedCompleter)
//                    .highlighter()
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scheduledFuture = ExecutorManager.getScheduledExecutor("terminal").scheduleAtFixedRate(() -> {
            String string = lineReader.readLine();

            Command command = BCore.getCommandManager().getCommand(string.split(" ")[0]);

            if (command == null) {
                Broker.getLogger().info("Unknown command. Type \"/help\" for help.");
                return;
            }

            BCore.getCommandManager().dispatch(new ConsoleSender(), string);
        }, 1, 1, TimeUnit.NANOSECONDS);
    }

    public void close() {
        this.scheduledFuture.cancel(true);
    }

    private void advancedCompleter(LineReader lineReader, ParsedLine parsedLine, List<Candidate> candidates) {
        List<String> line = parsedLine.words();

        if (line.isEmpty())
            return;

        String head = line.get(0);

        Command command = BCore.getCommandManager().getCommand(head);

        if (command == null)
            return;

        String[] parameter = {};

        if (line.size() != 1)
            parameter = Collections.singletonList(line).subList(1,line.size()).toArray(new String[0]);

        command.onTabComplete(new ConsoleSender(), command, head, parameter).stream()
                .filter(completion -> completion.startsWith(parsedLine.word()))
                .forEach(completion -> candidates.add(new Candidate(completion)));
    }
}
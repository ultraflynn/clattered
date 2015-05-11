package com.ultraflynn;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public final class CliMain {
    private static final String WELCOME_BANNER =
                    "  ____ _       _   _                    _\n" +
                    " / ___| | __ _| |_| |_ ___ _ __ ___  __| |\n" +
                    "| |   | |/ _` | __| __/ _ \\ '__/ _ \\/ _` |\n" +
                    "| |___| | (_| | |_| ||  __/ | |  __/ (_| |\n" +
                    " \\____|_|\\__,_|\\__|\\__\\___|_|  \\___|\\__,_|\n";
    private static final String PROMPT = "> ";

    private final InMemoryClattered clattered = new InMemoryClattered();

    public static void main(String[] args) {
        new CliMain().start();
    }

    private void start() {
        try {
            displayWelcome();
            displayPrompt();

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String s;
            while ((s = in.readLine()) != null && s.length() != 0) {
                ImmutableList<String> words = ImmutableList.copyOf(Splitter.on(" ").split(s));
                Optional<List<String>> messages = handleCommand(words);

                if (messages.isPresent()) {
                    displayMessages(messages.get());
                }
                displayPrompt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayWelcome() {
        System.out.println(WELCOME_BANNER);
    }

    private void displayPrompt() {
        System.out.print(PROMPT);
    }

    private Optional<List<String>> handleCommand(ImmutableList<String> words) {
        Optional<List<String>> messages = Optional.absent();
        String user = words.get(0);
        if (words.size() == 1) {
            // reading: <user name>
            messages = Optional.of(clattered.timeline(user));
        } else if (words.size() > 1) {
            String command = words.get(1);
            if (command.equals("->")) {
                // posting: <user name> -> <message>
                String text = Joiner.on(" ").join(words.subList(2, words.size()));
                clattered.publish(user, text);
            } else if (command.equals("follows")) {
                // following: <user name> follows <another user>
                String follow = words.size() == 2 ? "" : words.get(2);
                messages = Optional.fromNullable(clattered.follow(user, follow));
            } else if (command.equals("wall")) {
                // wall: <user name> wall
                messages = Optional.of(clattered.wall(user));
            }
        }
        return messages;
    }

    private void displayMessages(List<String> messages) {
        for (String message : messages) {
            System.out.println(message);
        }
    }
}

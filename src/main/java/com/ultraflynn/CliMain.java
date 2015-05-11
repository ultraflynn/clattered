package com.ultraflynn;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public final class CliMain {
    private static final String WELCOME_BANNER =
                    "  ____ _       _   _                    _\n" +
                    " / ___| | __ _| |_| |_ ___ _ __ ___  __| |\n" +
                    "| |   | |/ _` | __| __/ _ \\ '__/ _ \\/ _` |\n" +
                    "| |___| | (_| | |_| ||  __/ | |  __/ (_| |\n" +
                    " \\____|_|\\__,_|\\__|\\__\\___|_|  \\___|\\__,_|\n";
    private static final String PROMPT = "> ";

    private final BufferedReader input;
    private final PrintStream output;
    private final InMemoryClattered clattered = new InMemoryClattered();

    private boolean running = true;

    public static void main(String[] args) {
        new CliMain(new BufferedReader(new InputStreamReader(System.in)), System.out).start();
    }

    CliMain(BufferedReader input, PrintStream output) {
        this.input = input;
        this.output = output;
    }

    void start() {
        try {
            displayWelcome();
            displayPrompt();

            String s;
            while (running && (s = input.readLine()) != null && s.length() != 0) {
                ImmutableList<String> words = ImmutableList.copyOf(Splitter.on(" ").split(s));
                Optional<List<String>> messages = handleCommand(words);

                if (messages.isPresent()) {
                    displayMessages(messages.get());
                }
                // Only display the prompt when we are not quitting
                if (running) {
                    displayPrompt();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.close();
        }
    }

    private void displayWelcome() {
        output.println(WELCOME_BANNER);
    }

    private void displayPrompt() {
        output.print(PROMPT);
    }

    private Optional<List<String>> handleCommand(ImmutableList<String> words) {
        Optional<List<String>> messages = Optional.absent();

        String user = words.get(0);
        if (words.size() == 1) {
            if (user.equals("quit")) {
                running = false;
                messages = handleQuit();
            } else {
                messages = handleReading(user);
            }
        } else if (words.size() > 1) {
            String command = words.get(1);
            if (command.equals("->")) {
                handlingPosting(words, user);
            } else if (command.equals("follows")) {
                messages = handleFollow(words, user);
            } else if (command.equals("wall")) {
                messages = handleWall(user);
            }
        }
        return messages;
    }

    // quitting: quit
    private Optional<List<String>> handleQuit() {
        Optional<List<String>> messages;
        List<String> quitting = ImmutableList.of("Quitting");
        messages = Optional.of(quitting);
        return messages;
    }

    // reading: <user name>
    private Optional<List<String>> handleReading(String user) {
        Optional<List<String>> messages;
        messages = Optional.of(clattered.timeline(user));
        return messages;
    }

    // posting: <user name> -> <message>
    private void handlingPosting(ImmutableList<String> words, String user) {
        String text = Joiner.on(" ").join(words.subList(2, words.size()));
        clattered.publish(user, text);
    }

    // following: <user name> follows <another user>
    private Optional<List<String>> handleFollow(ImmutableList<String> words, String user) {
        Optional<List<String>> messages;
        String follow = words.size() == 2 ? "" : words.get(2);
        messages = Optional.fromNullable(clattered.follow(user, follow));
        return messages;
    }

    // wall: <user name> wall
    private Optional<List<String>> handleWall(String user) {
        Optional<List<String>> messages;
        messages = Optional.of(clattered.wall(user));
        return messages;
    }

    private void displayMessages(List<String> messages) {
        for (String message : messages) {
            output.println(message);
        }
    }
}

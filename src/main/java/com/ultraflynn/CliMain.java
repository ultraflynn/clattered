package com.ultraflynn;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CliMain {
    private final Clattered clattered = new Clattered();

    public static void main(String[] args) {
        CliMain main = new CliMain();
        main.start();
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

    private void displayPrompt() {
        System.out.print("> ");
    }

    private void displayWelcome() {
        System.out.println(
                "  ____ _       _   _                    _\n" +
                " / ___| | __ _| |_| |_ ___ _ __ ___  __| |\n" +
                "| |   | |/ _` | __| __/ _ \\ '__/ _ \\/ _` |\n" +
                "| |___| | (_| | |_| ||  __/ | |  __/ (_| |\n" +
                " \\____|_|\\__,_|\\__|\\__\\___|_|  \\___|\\__,_|\n");

    }

    private Optional<List<String>> handleCommand(ImmutableList<String> words) {
        Optional<List<String>> messages = Optional.absent();
        if (words.size() == 1) {
            // reading: <user name>
            messages = Optional.of(clattered.timeline(words.get(0)));
        } else if (words.size() > 1) {
            if (words.get(1).equals("->")) {
                // posting: <user name> -> <message>
                String text = Joiner.on(" ").join(words.subList(2, words.size()));
                clattered.publish(words.get(0), text);
            } else if (words.get(1).equals("follows")) {
                // following: <user name> follows <another user>
                clattered.follow(words.get(0), words.get(2));
            } else if (words.get(1).equals("wall")) {
                // wall: <user name> wall
                messages = Optional.of(clattered.wall(words.get(0)));
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

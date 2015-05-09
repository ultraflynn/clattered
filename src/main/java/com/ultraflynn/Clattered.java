package com.ultraflynn;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public class Clattered {
    private static DateFormat DATE_FORMAT = new DateFormat();

    private final Multimap<String, Message> usersMessages = ArrayListMultimap.create();
    private final Multimap<String, String> follows = ArrayListMultimap.create();

    public void publish(String user, String text) {
        if (!text.isEmpty()) {
            Message message = new Message(user, text);
            usersMessages.put(user, message);
        }
    }

    public List<String> timeline(String user) {
        DateTime now = DateTime.now();

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        List<Message> messages = ImmutableList.copyOf(usersMessages.get(user));
        for (Message message : Lists.reverse(messages)) {
            String elapsed = DATE_FORMAT.format(message.timestamp, now);

            builder.add(message.text + " (" + elapsed + ")");
        }
        return builder.build();
    }

    public void follow(String user, String follow) {
        // Don't add duplicate entries or there will be duplicates in the wall
        if (!follows.containsEntry(user, follow)) {
            follows.put(user, follow);
        }
    }

    public List<String> wall(String user) {
        DateTime now = DateTime.now();
        List<Message> messages = getMessagesForWall(user);

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (Message message : messages) {
            String elapsed = DATE_FORMAT.format(message.timestamp, now);
            builder.add(message.user + " - " + message.text + " (" + elapsed + ")");
        }
        return builder.build();
    }

    private List<Message> getMessagesForWall(String user) {
        List<Message> messages = Lists.newArrayList();

        messages.addAll(usersMessages.get(user));
        for (String following : follows.get(user)) {
            messages.addAll(usersMessages.get(following));
        }
        Collections.sort(messages);
        return messages;
    }
}

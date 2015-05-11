package com.ultraflynn;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

final class InMemoryClattered implements Clattered {
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

    public List<String> follow(String user, String follow) {
        // Return who a user is following if they do not specify who they wish to follow
        if (follow.isEmpty()) {
            return ImmutableList.copyOf(follows.get(user));
        }

        // A user cannot follow themselves
        if (user.equals(follow)) {
            return null;
        }

        // A user can only follow users who have posted messages
        if (!usersMessages.containsKey(follow)) {
            return null;
        }

        // A user cannot follow the same user twice
        if (follows.containsEntry(user, follow)) {
            return null;
        }

        follows.put(user, follow);
        return null;
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

package com.ultraflynn;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.joda.time.DateTime;

import java.util.List;

public class Clattered {
    private static DateFormat DATE_FORMAT = new DateFormat();

    private final Multimap<String, Message> usersMessages = ArrayListMultimap.create();

    public void publish(String user, String text) {
        Message message = new Message(text);
        usersMessages.put(user, message);
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
    }

    public List<String> wall(String user) {
        return ImmutableList.of();
    }
}

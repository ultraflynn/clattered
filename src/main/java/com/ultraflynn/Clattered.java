package com.ultraflynn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class Clattered {
    private final List<String> messages = Lists.newArrayList();

    public void publish(String user, String message) {
        messages.add(message);
    }

    public List<String> timeline(String user) {
        // TODO Add the correct time offset
        return ImmutableList.copyOf(messages);
    }

    public void follow(String user, String follow) {
    }

    public List<String> wall(String user) {
        return ImmutableList.of();
    }
}

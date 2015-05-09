package com.ultraflynn;

import org.joda.time.DateTime;

// All fields are immutable so getters are omitted
class Message implements Comparable<Message> {
    final DateTime timestamp = DateTime.now();
    final String user;
    final String text;

    Message(String user, String text) {
        this.user = user;
        this.text = text;
    }

    @Override
    public int compareTo(Message o) {
        long diff = timestamp.getMillis() - o.timestamp.getMillis();
        return diff > 0 ? -1 : diff < 0 ? 1 : 0;
    }
}

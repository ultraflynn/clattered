package com.ultraflynn;

import org.joda.time.DateTime;

// All fields are immutable so getters are omitted
class Message {
    final DateTime timestamp = DateTime.now();
    final String text;

    Message(String text) {
        this.text = text;
    }
}

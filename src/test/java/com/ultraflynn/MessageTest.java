package com.ultraflynn;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class MessageTest {
    private Message message;

    @Before
    public void setUp() {
        message = new Message("Alice", "I love the weather today");
    }

    @Test
    public void shouldStoreUser() {
        assertThat(message.user, is("Alice"));
    }

    @Test
    public void shouldStoreMessageText() {
        assertThat(message.text, is("I love the weather today"));
    }

    @Test
    public void shouldStoreTimestamp() {
        assertThat(message.timestamp, is(not(nullValue())));
    }
}
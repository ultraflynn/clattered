package com.ultraflynn;

import org.hamcrest.Matcher;
import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AcceptanceTest {
    private static final long ONE_SECOND_IN_MILLIS = 1000;

    private Clattered clattered;

    @Before
    public void setUp() {
        clattered = new Clattered();
    }

    @Test
    public void shouldPassBasicUsageTest() {
        DateTimeUtils.setCurrentMillisFixed(0);

        // Posting: Alice can publish messages to a personal timeline
        publish(minutes(5), "Alice", "I love the weather today");
        publish(minutes(2), "Bob", "Damn! We lost!");
        publish(minutes(1), "Bob", "Good game though.");

        // Reading: Bob can view Alice’s timeline
        List<String> alicesMessages = clattered.timeline("Alice");
        assertWall(alicesMessages, at(0), is("I love the weather today (5 minutes ago)"));

        List<String> bobsMessages = clattered.timeline("Bob");
        assertWall(bobsMessages, at(0), is("Good game though. (1 minute ago)"));
        assertWall(bobsMessages, at(1), is("Damn! We lost! (2 minutes ago)"));

        // Following: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions
        publish(seconds(15), "Charlie", "I'm in New York today! Anyone want to have a coffee?");
        clattered.follow("Charlie", "Alice");

        List<String> charliesWall = clattered.wall("Charlie");
        assertWall(charliesWall, at(0), is("Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)"));
        assertWall(charliesWall, at(1), is("Alice - I love the weather today (5 minutes ago)"));

        clattered.follow("Charlie", "Bob");
        charliesWall = clattered.wall("Charlie");
        assertWall(charliesWall, at(0), is("Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)"));
        assertWall(charliesWall, at(1), is("Bob - Good game though. (1 minute ago)"));
        assertWall(charliesWall, at(2), is("Bob - Damn! We lost! (2 minutes ago)"));
        assertWall(charliesWall, at(3), is("Alice - I love the weather today (5 minutes ago)"));
    }

    private static void assertWall(List<String> messages, int offset, Matcher<String> message) {
        if (offset >= messages.size()) {
            fail(messages + " has no message at " + offset);
        }
        assertThat(messages.get(offset), message);
    }

    // Syntactic sugar to help with readability of the assertion
    private static int at(int offset) {
        return offset;
    }

    private static long minutes(int minutes) {
        return minutes * 60 * ONE_SECOND_IN_MILLIS;
    }

    private static long seconds(int seconds) {
        return seconds * ONE_SECOND_IN_MILLIS;
    }

    private void publish(long millisToAdd, String user, String message) {
        DateTimeUtils.setCurrentMillisFixed(millisToAdd);
        clattered.publish(user, message);
    }
}

package com.ultraflynn;

import org.hamcrest.Matcher;
import org.joda.time.DateTimeUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ultraflynn.TimeConstants.minutes;
import static com.ultraflynn.TimeConstants.seconds;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

public class AcceptanceTest {
    private Clattered clattered;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(0);
        clattered = new Clattered();
    }

    @Test
    public void shouldPassBasicUsageTest() {
        // Posting: Alice can publish messages to a personal timeline
        publish("Alice", "I love the weather today", minutes(3));
        publish("Bob", "Damn! We lost!", minutes(4));
        publish("Bob", "Good game though.", minutes(5));

        // Reading: Bob can view Alice’s timeline
        List<String> alicesTimeline = clattered.timeline("Alice");
        assertThat(alicesTimeline, at(0), is("I love the weather today (5 minutes ago)"));

        List<String> bobsTimeline = clattered.timeline("Bob");
        assertThat(bobsTimeline, at(0), is("Good game though. (1 minute ago)"));
        assertThat(bobsTimeline, at(1), is("Damn! We lost! (2 minutes ago)"));

        // Following: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions
        publish("Charlie", "I'm in New York today! Anyone want to have a coffee?", minutes(5) + seconds(15));
        clattered.follow("Charlie", "Alice");

        List<String> charliesWall = clattered.wall("Charlie");
        assertThat(charliesWall, at(0), is("Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)"));
        assertThat(charliesWall, at(1), is("Alice - I love the weather today (5 minutes ago)"));

        clattered.follow("Charlie", "Bob");
        charliesWall = clattered.wall("Charlie");
        assertThat(charliesWall, at(0), is("Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)"));
        assertThat(charliesWall, at(1), is("Bob - Good game though. (1 minute ago)"));
        assertThat(charliesWall, at(2), is("Bob - Damn! We lost! (2 minutes ago)"));
        assertThat(charliesWall, at(3), is("Alice - I love the weather today (5 minutes ago)"));
    }

    private static void assertThat(List<String> messages, int offset, Matcher<String> message) {
        if (offset >= messages.size()) {
            fail(messages + " has no message at " + offset);
        }
        Assert.assertThat(messages.get(offset), message);
    }

    // Syntactic sugar to help with readability of the assertion
    private static int at(int offset) {
        return offset;
    }

    private void publish(String user, String message, long millisToAdd) {
        clattered.publish(user, message);
        DateTimeUtils.setCurrentMillisFixed(millisToAdd);
    }
}

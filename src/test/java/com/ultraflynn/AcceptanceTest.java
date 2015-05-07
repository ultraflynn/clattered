package com.ultraflynn;

import org.joda.time.DateTimeUtils;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AcceptanceTest {
    private static final long ONE_MINUTE = 60 * 1000;

    @Test
    public void shouldPassBasicUsageTest() {
        DateTimeUtils.setCurrentMillisFixed(0);

        // TODO Throughout this test set the current time appropriately

        Clattered clattered = new Clattered();

        // Posting: Alice can publish messages to a personal timeline
        clattered.publish("Alice", "I love the weather today");
        clattered.publish("Bob", "Damn! We lost!");
        clattered.publish("Bob", "Good game though.");

        // Reading: Bob can view Alice’s timeline
        List<String> alicesMessages = clattered.timeline("Alice");
        assertThat(alicesMessages.get(0), is("I love the weather today (5 minutes ago)"));

        List<String> bobsMessages = clattered.timeline("Bob");
        assertThat(bobsMessages.get(0), is("Good game though. (1 minute ago)"));
        assertThat(bobsMessages.get(1), is("Damn! We lost! (2 minutes ago)"));

        // Following: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions
        clattered.publish("Charlie", "I'm in New York today! Anyone want to have a coffee?");
        clattered.follow("Charlie", "Alice");

        List<String> charliesWall = clattered.wall("Charlie");
        assertThat(charliesWall.get(0), is("Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)"));
        assertThat(charliesWall.get(1), is("Alice - I love the weather today (5 minutes ago)"));

        clattered.follow("Charlie", "Bob");
        charliesWall = clattered.wall("Charlie");
        assertThat(charliesWall.get(0), is("Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)"));
        assertThat(charliesWall.get(1), is("Bob - Good game though. (1 minute ago)"));
        assertThat(charliesWall.get(2), is("Bob - Damn! We lost! (2 minutes ago)"));
        assertThat(charliesWall.get(3), is("Alice - I love the weather today (5 minutes ago)"));
    }

    private static void currentTime(int n) {
        DateTimeUtils.setCurrentMillisFixed(n * ONE_MINUTE);
    }
}

package com.ultraflynn;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ultraflynn.TimeHelper.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClatteredTest {
    private Clattered clattered;

    @Before
    public void setUp() {
        currentTime(0);
        clattered = new Clattered();
    }

    @Test
    public void shouldAllowUserToPublishMessageToTheirTimeline() {
        clattered.publish("Alice", "I love the weather today");
        currentTime(minutes(5));

        List<String> timeline = clattered.timeline("Alice");
        assertThat(timeline.size(), is(1));
        assertThat(timeline.get(0), is("I love the weather today (5 minutes ago)"));
    }

    @Test
    public void shouldAllowUserToPublishTwoMessagesToTheirTimeline() {
        clattered.publish("Bob", "Damn! We lost!");
        currentTime(minutes(1));
        clattered.publish("Bob", "Good game though.");
        currentTime(minutes(2));

        List<String> timeline = clattered.timeline("Bob");
        assertThat(timeline.size(), is(2));
        assertThat(timeline.get(0), is("Good game though. (1 minute ago)"));
        assertThat(timeline.get(1), is("Damn! We lost! (2 minutes ago)"));
    }

    @Test
    public void shouldIgnoreRequestToPublishBlankMessage() {
        clattered.publish("Alice", "");
        currentTime(minutes(5));

        List<String> timeline = clattered.timeline("Alice");
        assertThat(timeline.size(), is(0));
    }

    @Test
    public void shouldAllowTwoUsersToPublishMessagesToTheirTimelines() {
        clattered.publish("Alice", "I love the weather today");
        currentTime(minutes(3));
        clattered.publish("Bob", "Damn! We lost!");
        currentTime(minutes(4));
        clattered.publish("Bob", "Good game though.");
        currentTime(minutes(5));

        List<String> alicesTimeline = clattered.timeline("Alice");
        assertThat(alicesTimeline.size(), is(1));
        assertThat(alicesTimeline.get(0), is("I love the weather today (5 minutes ago)"));

        List<String> bobsTimeline = clattered.timeline("Bob");
        assertThat(bobsTimeline.size(), is(2));
        assertThat(bobsTimeline.get(0), is("Good game though. (1 minute ago)"));
        assertThat(bobsTimeline.get(1), is("Damn! We lost! (2 minutes ago)"));
    }

    @Test
    public void shouldAllowUserToFollowAnotherAndSeeTheirMessagesOnTheirWall() {
        clattered.publish("Alice", "I love the weather today");
        currentTime(minutes(5));
        clattered.publish("Charlie", "I'm in New York today! Anyone want to have a coffee?");
        currentTime(minutes(5) + seconds(2));

        clattered.follow("Charlie", "Alice");

        List<String> wall = clattered.wall("Charlie");
        assertThat(wall.size(), is(2));
        assertThat(wall.get(0), is("Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)"));
        assertThat(wall.get(1), is("Alice - I love the weather today (5 minutes ago)"));
    }

    @Test
    public void shouldAllowUsersToFollowMoreThanOneUser() {
        clattered.publish("Alice", "I love the weather today");
        currentTime(minutes(5));
        clattered.publish("Charlie", "I'm in New York today! Anyone want to have a coffee?");
        currentTime(minutes(5) + seconds(2));
        clattered.publish("Bob", "Good game though.");
        currentTime(minutes(5) + seconds(12));

        clattered.follow("Charlie", "Alice");
        clattered.follow("Charlie", "Bob");

        List<String> wall = clattered.wall("Charlie");
        assertThat(wall.size(), is(3));
        assertThat(wall.get(0), is("Bob - Good game though. (10 seconds ago)"));
        assertThat(wall.get(1), is("Charlie - I'm in New York today! Anyone want to have a coffee? (12 seconds ago)"));
        assertThat(wall.get(2), is("Alice - I love the weather today (5 minutes ago)"));
    }

    @Test
    public void shouldIgnoreRequestToFollowSameUserMoreThanOnce() {
        clattered.publish("Alice", "I love the weather today");
        currentTime(minutes(5));
        clattered.publish("Charlie", "I'm in New York today! Anyone want to have a coffee?");
        currentTime(minutes(5) + seconds(2));
        clattered.publish("Bob", "Good game though.");
        currentTime(minutes(5) + seconds(12));

        clattered.follow("Charlie", "Alice");
        clattered.follow("Charlie", "Bob");
        clattered.follow("Charlie", "Alice");

        List<String> wall = clattered.wall("Charlie");
        assertThat(wall.size(), is(3));
        assertThat(wall.get(0), is("Bob - Good game though. (10 seconds ago)"));
        assertThat(wall.get(1), is("Charlie - I'm in New York today! Anyone want to have a coffee? (12 seconds ago)"));
        assertThat(wall.get(2), is("Alice - I love the weather today (5 minutes ago)"));
    }
}
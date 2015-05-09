package com.ultraflynn;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ultraflynn.TimeConstants.minutes;
import static com.ultraflynn.TimeConstants.seconds;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClatteredTest {
    private Clattered clattered;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(0);
        clattered = new Clattered();
    }

    @Test
    public void shouldAllowUserToPublishMessageToTheirTimeline() {
        clattered.publish("Alice", "I love the weather today");
        DateTimeUtils.setCurrentMillisFixed(minutes(5));

        List<String> timeline = clattered.timeline("Alice");
        assertThat(timeline.size(), is(1));
        assertThat(timeline.get(0), is("I love the weather today (5 minutes ago)"));
    }

    @Test
    public void shouldAllowUserToPublishTwoMessagesToTheirTimeline() {
        clattered.publish("Bob", "Damn! We lost!");
        DateTimeUtils.setCurrentMillisFixed(minutes(1));
        clattered.publish("Bob", "Good game though.");
        DateTimeUtils.setCurrentMillisFixed(minutes(2));

        List<String> timeline = clattered.timeline("Bob");
        assertThat(timeline.size(), is(2));
        assertThat(timeline.get(0), is("Good game though. (1 minute ago)"));
        assertThat(timeline.get(1), is("Damn! We lost! (2 minutes ago)"));
    }

    @Test
    public void shouldAllowTwoUsersToPublishMessagesToTheirTimelines() {
        clattered.publish("Alice", "I love the weather today");
        DateTimeUtils.setCurrentMillisFixed(minutes(3));
        clattered.publish("Bob", "Damn! We lost!");
        DateTimeUtils.setCurrentMillisFixed(minutes(4));
        clattered.publish("Bob", "Good game though.");
        DateTimeUtils.setCurrentMillisFixed(minutes(5));

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
        DateTimeUtils.setCurrentMillisFixed(minutes(5));
        clattered.publish("Charlie", "I'm in New York today! Anyone want to have a coffee?");
        DateTimeUtils.setCurrentMillisFixed(minutes(5) + seconds(2));

        clattered.follow("Charlie", "Alice");
        List<String> wall = clattered.wall("Charlie");
        assertThat(wall.size(), is(2));
        assertThat(wall.get(0), is("Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)"));
        assertThat(wall.get(1), is("Alice - I love the weather today (5 minutes ago)"));
    }
}
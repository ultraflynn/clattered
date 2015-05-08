package com.ultraflynn;

import org.joda.time.DateTimeUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ultraflynn.TimeConstants.minutes;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ClatteredTest {
    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(0);
    }

    @Test
    public void shouldAllowUserToPublishMessageToTheirTimeline() {
        Clattered clattered = new Clattered();
        clattered.publish("Alice", "I love the weather today");

        DateTimeUtils.setCurrentMillisFixed(minutes(5));
        List<String> timeline = clattered.timeline("Alice");
        assertThat(timeline.size(), is(1));
        assertThat(timeline.get(0), is("I love the weather today (5 minutes ago)"));
    }
}
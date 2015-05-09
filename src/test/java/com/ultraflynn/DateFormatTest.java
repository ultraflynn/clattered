package com.ultraflynn;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateFormatTest {
    @Test
    public void shouldFormatAmountOfMinutesAgo() {
        String pattern = "dd-MMM-yy hh:mm:ss aa";

        String start = "09-MAY-15 09:17:00 AM";
        LocalDateTime startDateTime = LocalDateTime.parse(start, DateTimeFormat.forPattern(pattern));
        String end = "09-MAY-15 09:18:00 AM";
        LocalDateTime endDateTime = LocalDateTime.parse(end, DateTimeFormat.forPattern(pattern));

        DateFormat dateFormat = new DateFormat();
        String formatted = dateFormat.format(startDateTime.toDateTime(), endDateTime.toDateTime());

        assertThat(formatted, is("1 minute ago"));
    }
}
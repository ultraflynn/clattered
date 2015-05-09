package com.ultraflynn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.List;

public class Clattered {
    private static final PeriodFormatter DATE_FORMATTER = new PeriodFormatterBuilder()
            .appendSeconds().appendSuffix(" seconds ago")
            .appendMinutes().appendSuffix(" minutes ago")
            .appendHours().appendSuffix(" hours ago")
            .appendDays().appendSuffix(" days ago")
            .appendWeeks().appendSuffix(" weeks ago")
            .appendMonths().appendSuffix(" months ago")
            .appendYears().appendSuffix(" years ago")
            .printZeroNever()
            .toFormatter();

    private final List<Message> messages = Lists.newArrayList();

    public void publish(String user, String text) {
        Message message = new Message(text);
        messages.add(message);
    }

    public List<String> timeline(String user) {
        DateTime now = DateTime.now();

        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (Message message : Lists.reverse(messages)) {
            Period period = new Period(message.timestamp, now);
            String elapsed = DATE_FORMATTER.print(period);

            builder.add(message.text + " (" + elapsed + ")");
        }
        return builder.build();
    }

    public void follow(String user, String follow) {
    }

    public List<String> wall(String user) {
        return ImmutableList.of();
    }
}

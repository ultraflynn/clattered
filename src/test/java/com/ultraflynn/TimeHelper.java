package com.ultraflynn;

import org.joda.time.DateTimeUtils;

class TimeHelper {
    static final long ONE_SECOND_IN_MILLIS = 1000;

    static long minutes(int minutes) {
        return minutes * 60 * ONE_SECOND_IN_MILLIS;
    }

    static long seconds(int seconds) {
        return seconds * ONE_SECOND_IN_MILLIS;
    }

    static void currentTime(long current) {
        DateTimeUtils.setCurrentMillisFixed(current);
    }
}

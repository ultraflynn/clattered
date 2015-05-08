package com.ultraflynn;

class TimeConstants {
    static final long ONE_SECOND_IN_MILLIS = 1000;

    static long minutes(int minutes) {
        return minutes * 60 * ONE_SECOND_IN_MILLIS;
    }

    static long seconds(int seconds) {
        return seconds * ONE_SECOND_IN_MILLIS;
    }
}

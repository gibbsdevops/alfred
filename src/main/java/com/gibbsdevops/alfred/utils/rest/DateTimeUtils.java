package com.gibbsdevops.alfred.utils.rest;

import java.time.Clock;

public class DateTimeUtils {

    private static Clock clock = Clock.systemUTC();

    public static Clock getClock() {
        return clock;
    }

    public static void setClock(Clock c) {
        clock = c;
    }
}

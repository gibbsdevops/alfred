package com.gibbsdevops.alfred.model.github.utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class GHTimeFormat {

    // e.g. '2015-02-23T03:00:16Z'
    private static final DateTimeFormatter df = DateTimeFormatter.ISO_INSTANT;

    public static final long parse(String s) {
        return Instant.from(df.parse(s)).getEpochSecond();
    }

}

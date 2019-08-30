package com.n26.exercise.validator;

import java.util.Date;

public final class TimeValidator {

    public static final long timeToLive = 60000;

    private TimeValidator(){

    }
    public static boolean withinLastMinute(final long lastAccessedTime) {
        final long now = System.currentTimeMillis();
        return (now - lastAccessedTime) < timeToLive;
    }

    public static boolean isInFuture(final Date lastAccessdate) {
        return new Date().before(lastAccessdate);
    }
}

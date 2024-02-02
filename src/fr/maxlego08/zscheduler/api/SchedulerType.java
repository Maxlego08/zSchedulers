package fr.maxlego08.zscheduler.api;

import java.util.concurrent.TimeUnit;

public enum SchedulerType {

    HOURLY,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,

    EVERY_DAY,
    EVERY_HOUR,
    EVERY_MINUTE,
    EVERY_SECOND,

    ;

    public boolean isRepeatScheduler() {
        return this == EVERY_DAY || this == EVERY_HOUR || this == EVERY_MINUTE || this == EVERY_SECOND;
    }

    public TimeUnit convertToTimeUnit() {
        switch (this) {
            case EVERY_DAY:
                return TimeUnit.DAYS;
            case EVERY_HOUR:
                return TimeUnit.HOURS;
            case EVERY_MINUTE:
                return TimeUnit.MINUTES;
            case EVERY_SECOND:
                return TimeUnit.SECONDS;
            default:
                return null;
        }
    }

}

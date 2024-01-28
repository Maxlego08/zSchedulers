package fr.maxlego08.zscheduler.api;

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

}

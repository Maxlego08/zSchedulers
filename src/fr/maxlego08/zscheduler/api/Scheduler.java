package fr.maxlego08.zscheduler.api;

import java.util.Date;
import java.util.Map;

public interface Scheduler {

    void start();

    void disable();

    String getName();

    void setImplementation(Implementation implementation);

    Implementation getImplementation();

    String getImplementationName();

    String getFormattedTimeUntilNextTask();

    Date getNextDate();

    Map<String, Object> getImplementationValues();

}

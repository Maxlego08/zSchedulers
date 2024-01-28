package fr.maxlego08.zscheduler.api;

import fr.maxlego08.zscheduler.api.schedulers.ClassicScheduler;

public interface Implementation {

    /**
     * Get implementation name
     *
     * @return name
     */
    String getName();

    /**
     * Schedule task
     *
     * @param scheduler - {@link ClassicScheduler}
     */
    void schedule(Scheduler scheduler);

}

package fr.maxlego08.zscheduler.api;

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
     * @param scheduler - {@link Scheduler}
     */
    void schedule(Scheduler scheduler);

}

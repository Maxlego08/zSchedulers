package fr.maxlego08.zscheduler.api;

import java.util.List;
import java.util.Optional;

public interface SchedulerManager {

    /**
     * Register a new implementation
     * When registering your implementation it will be automatically added to each scheduler that has the name of your implementation
     *
     * @param implementation - {@link Implementation}
     * @return true if register is a success
     */
    boolean registerImplementation(Implementation implementation);

    /**
     * Get an {@link Implementation} by name
     *
     * @param name - Implementation name
     * @return optional of {@link Implementation}
     */
    Optional<Implementation> getImplementation(String name);

    /**
     * Get a {@link Scheduler} by name
     *
     * @param name - Scheduler name
     * @return optional of {@link Scheduler}
     */
    Optional<Scheduler> getScheduler(String name);

    /**
     * Get schedulers
     *
     * @return schedulers
     */
    List<Scheduler> getSchedulers();

}

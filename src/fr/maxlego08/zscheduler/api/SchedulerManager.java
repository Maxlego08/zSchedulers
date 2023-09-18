package fr.maxlego08.zscheduler.api;

import java.util.Optional;

public interface SchedulerManager {

    /**
     * Register a new implementation
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

}

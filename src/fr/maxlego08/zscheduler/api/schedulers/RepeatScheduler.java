package fr.maxlego08.zscheduler.api.schedulers;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.api.Implementation;
import fr.maxlego08.zscheduler.api.Scheduler;
import fr.maxlego08.zscheduler.api.SchedulerType;
import fr.maxlego08.zscheduler.zcore.logger.Logger;
import fr.maxlego08.zscheduler.zcore.utils.builder.TimerBuilder;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RepeatScheduler implements Scheduler {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final SchedulerPlugin plugin;
    private final String name;
    private final SchedulerType schedulerType;
    private final long initialDelay;
    private final long period;
    private final int minPlayer;
    private final TimeUnit timeUnit;
    private final List<String> commands;
    private final String implementationName;
    private final Map<String, Object> implementationValues;
    private Implementation implementation;
    private ScheduledFuture<?> scheduledFuture;

    public RepeatScheduler(SchedulerPlugin plugin, String name, SchedulerType schedulerType, long initialDelay, long period, int minPlayer, List<String> commands, String implementationName, Map<String, Object> implementationValues) {
        this.plugin = plugin;
        this.name = name;
        this.schedulerType = schedulerType;
        this.initialDelay = initialDelay;
        this.period = period;
        this.minPlayer = minPlayer;
        this.commands = commands;
        this.implementationName = implementationName;
        this.implementationValues = implementationValues;

        switch (schedulerType) {
            case EVERY_DAY:
                this.timeUnit = TimeUnit.DAYS;
                break;
            case EVERY_MINUTE:
                this.timeUnit = TimeUnit.MINUTES;
                break;
            case EVERY_SECOND:
                this.timeUnit = TimeUnit.SECONDS;
                break;
            case EVERY_HOUR:
            default:
                this.timeUnit = TimeUnit.HOURS;
                break;
        }
    }

    @Override
    public void start() {
        this.scheduledFuture = this.executor.scheduleAtFixedRate(() -> {

            plugin.getServerImplementation().runNextTick(() -> {
                int online = Bukkit.getOnlinePlayers().size();
                if (online >= minPlayer) {
                    for (String command : commands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }

                    if (implementation != null) {
                        implementation.schedule(this);
                    }
                } else {
                    Logger.info("Not enough players to execute the scheduler " + name + ", minimum player: " + minPlayer, Logger.LogType.INFO);
                }
            });

        }, this.initialDelay, this.period, this.timeUnit);
    }

    @Override
    public void disable() {
        this.executor.shutdown();
        try {
            if (!this.executor.awaitTermination(60, TimeUnit.SECONDS)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException ignored) {
            this.executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Implementation getImplementation() {
        return this.implementation;
    }

    @Override
    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

    @Override
    public String getImplementationName() {
        return this.implementationName;
    }

    @Override
    public String getFormattedTimeUntilNextTask() {
        Date date = getNextDate();
        return TimerBuilder.getStringTime((date.getTime() - new GregorianCalendar().getTimeInMillis()) / 1000);
    }

    @Override
    public Date getNextDate() {
        long nextExecutionTimeInMillis = System.currentTimeMillis() + this.scheduledFuture.getDelay(TimeUnit.MILLISECONDS);
        return new Date(nextExecutionTimeInMillis);
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    public SchedulerPlugin getPlugin() {
        return plugin;
    }

    public SchedulerType getSchedulerType() {
        return schedulerType;
    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public long getPeriod() {
        return period;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public List<String> getCommands() {
        return commands;
    }

    @Override
    public Map<String, Object> getImplementationValues() {
        return implementationValues;
    }
}

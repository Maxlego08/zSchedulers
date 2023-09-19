package fr.maxlego08.zscheduler.api;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.zcore.logger.Logger;
import fr.maxlego08.zscheduler.zcore.utils.builder.TimerBuilder;
import org.bukkit.Bukkit;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

    private final SchedulerPlugin plugin;
    private final String name;
    private final SchedulerType schedulerType;
    private final int dayOfMonth;
    private final int dayOfWeek;
    private final int month;
    private final int hour;
    private final int minute;
    private final int minPlayer;
    private final List<String> commands;
    private final String implementationName;
    private Implementation implementation;

    private Calendar calendar = null;
    private final Timer timer = new Timer();
    private final Map<String, Object> implementationValues;

    public Scheduler(SchedulerPlugin plugin, String name, SchedulerType schedulerType, int dayOfMonth, int dayOfWeek, int month, int hour, int minute, int minPlayer, List<String> commands, Implementation implementation, String implementationName, Map<String, Object> implementationValues) {
        this.plugin = plugin;
        this.name = name;
        this.schedulerType = schedulerType;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.hour = hour;
        this.minute = minute;
        this.minPlayer = minPlayer;
        this.commands = commands;
        this.implementation = implementation;
        this.implementationName = implementationName;
        this.implementationValues = implementationValues;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Map<String, Object> getImplementationValues() {
        return implementationValues;
    }

    public String getName() {
        return name;
    }

    public SchedulerType getSchedulerType() {
        return schedulerType;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getMonth() {
        return month;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public List<String> getCommands() {
        return commands;
    }

    public Implementation getImplementation() {
        return implementation;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void disable() {
        timer.cancel();
    }

    private void nextScheduler() {
        switch (schedulerType) {
            case HOURLY:
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case DAILY:
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case WEEKLY:
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                break;
            case MONTHLY:
                calendar.add(Calendar.MONTH, 1);
                break;
            case YEARLY:
                calendar.add(Calendar.YEAR, 1);
                break;
        }
    }

    public void start() {

        Scheduler scheduler = this;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                plugin.getServerImplementation().runNextTick(() -> {
                    int online = Bukkit.getOnlinePlayers().size();
                    if (online >= minPlayer) {
                        for (String command : commands) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                        }

                        if (implementation != null) {
                            implementation.schedule(scheduler);
                        }
                    } else {
                        Logger.info("Not enough players to execute the scheduler " + name + ", minimum player: " + minPlayer, Logger.LogType.INFO);
                    }

                    nextScheduler();
                });
            }
        };

        Calendar now = new GregorianCalendar();
        switch (schedulerType) {
            case HOURLY:
                calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.HOUR_OF_DAY), minute);
                break;
            case DAILY:
                calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), hour, minute);
                break;
            case WEEKLY:
                calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), hour, minute);
                int currentDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
                int daysUntilNextTargetDay = (dayOfWeek - currentDayOfWeek + 7) % 7;

                calendar.add(Calendar.DAY_OF_MONTH, daysUntilNextTargetDay);
                break;
            case MONTHLY:
                calendar = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), dayOfMonth, hour, minute);
                break;
            case YEARLY:
                calendar = new GregorianCalendar(now.get(Calendar.YEAR), month, dayOfMonth, hour, minute);
                break;
        }

        if (calendar.before(now)) {
            nextScheduler();
        }

        switch (schedulerType) {
            case HOURLY:
                timer.schedule(task, calendar.getTime(), 60 * 60 * 1000);
                break;
            case DAILY:
                timer.schedule(task, calendar.getTime(), 24 * 60 * 60 * 1000);
                break;
            case WEEKLY:
                timer.schedule(task, calendar.getTime(), 7 * 24 * 60 * 60 * 1000);
                break;
            case MONTHLY:
                timer.schedule(task, calendar.getTime(), 30L * 24L * 60L * 60L * 1000L);
                break;
            case YEARLY:
                timer.schedule(task, calendar.getTime(), 365L * 24L * 60L * 60L * 1000L);
                break;
        }
    }

    public String getFormattedTimeUntilNextTask() {
        return TimerBuilder.getStringTime((calendar.getTimeInMillis() - new GregorianCalendar().getTimeInMillis()) / 1000);
    }

    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

    public String getImplementationName() {
        return implementationName;
    }
}

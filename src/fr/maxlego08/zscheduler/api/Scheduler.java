package fr.maxlego08.zscheduler.api;

import fr.maxlego08.zscheduler.zcore.logger.Logger;
import fr.maxlego08.zscheduler.zcore.utils.builder.TimerBuilder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

    private final Plugin plugin;
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

    public Scheduler(Plugin plugin, String name, SchedulerType schedulerType, int dayOfMonth, int dayOfWeek, int month, int hour, int minute, int minPlayer, List<String> commands, Implementation implementation, String implementationName) {
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
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
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

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    int online = Bukkit.getOnlinePlayers().size();
                    System.out.println(online + " >= " + minPlayer);
                    if (online >= minPlayer) {
                        for (String command : commands) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
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
        Calendar now = new GregorianCalendar();
        long timeInMillis = calendar.getTimeInMillis() - now.getTimeInMillis();

        System.out.println(timeInMillis + " < 0 = " + (timeInMillis < 0));

        if (timeInMillis < 0) {
            switch (schedulerType) {
                case DAILY:
                    timeInMillis += 24 * 60 * 60 * 1000;
                    break;
                case WEEKLY:
                    timeInMillis += 7 * 24 * 60 * 60 * 1000;
                    break;
                case MONTHLY:
                    timeInMillis += 30L * 24L * 60L * 60L * 1000L;
                    break;
                case YEARLY:
                    timeInMillis += 365L * 24L * 60L * 60L * 1000L;
                    break;
                default:
                    break;
            }
        }

        long seconds = timeInMillis / 1000;
        return TimerBuilder.getStringTime(seconds);
    }

    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

    public String getImplementationName() {
        return implementationName;
    }
}

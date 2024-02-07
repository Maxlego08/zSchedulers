package fr.maxlego08.zscheduler.loader;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.api.Implementation;
import fr.maxlego08.zscheduler.api.Scheduler;
import fr.maxlego08.zscheduler.api.schedulers.ClassicScheduler;
import fr.maxlego08.zscheduler.api.SchedulerType;
import fr.maxlego08.zscheduler.api.schedulers.RepeatScheduler;
import fr.maxlego08.zscheduler.zcore.logger.Logger;
import fr.maxlego08.zscheduler.zcore.utils.loader.Loader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.text.DateFormatSymbols;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class SchedulerLoader implements Loader<Scheduler> {

    private static final Map<String, Integer> DAYS = IntStream.range(1, 8).collect(HashMap::new, (map, month) -> map.put(new DateFormatSymbols(Locale.ENGLISH).getWeekdays()[month].toUpperCase(), month), HashMap::putAll);
    private static final Map<String, Integer> MONTHS = IntStream.range(0, 11).collect(HashMap::new, (map, month) -> map.put(new DateFormatSymbols(Locale.ENGLISH).getMonths()[month].toUpperCase(), month), HashMap::putAll);

    private final SchedulerPlugin plugin;
    private final String name;
    private final long previousExecution;

    public SchedulerLoader(SchedulerPlugin plugin, String name, long previousExecution) {
        this.plugin = plugin;
        this.name = name;
        this.previousExecution = previousExecution;
    }

    @Override
    public Scheduler load(YamlConfiguration configuration, String path) {

        String typeAsString = configuration.getString(path + "type", "");
        SchedulerType schedulerType;
        try {
            schedulerType = SchedulerType.valueOf(typeAsString.toUpperCase());
        } catch (Exception exception) {
            Logger.info("Impossible to find the type of " + path, Logger.LogType.ERROR);
            return null;
        }

        int dayOfMonth = 0;
        int dayOfWeek = 0;
        int month = 0;
        int hour = configuration.getInt(path + "hour", 0);
        int minute = configuration.getInt(path + "minute", 0);
        int second = configuration.getInt(path + "second", 0);
        int period = configuration.getInt(path + "period", 0);
        int initialDelay = configuration.getInt(path + "initialDelay", 0);
        boolean saveTimer = configuration.getBoolean(path + "saveTimer", false);
        int minPlayer = configuration.getInt(path + "minPlayer");
        List<String> commands = configuration.getStringList(path + "commands");
        ConfigurationSection implementationSection = configuration.getConfigurationSection(path + "implementation.");
        Implementation implementation = null;
        String implementationName = "";
        Map<String, Object> implementationValues = new HashMap<>();

        if (implementationSection != null) {
            for (String key : implementationSection.getKeys(false)) {
                if (key.equals("name")) {
                    implementationName = configuration.getString(path + "implementation." + key);
                } else implementationValues.put(key, configuration.get(path + "implementation." + key));
            }

            if (implementationName != null) {
                Optional<Implementation> optional = plugin.getManager().getImplementation(implementationName);
                if (optional.isPresent()) {
                    implementation = optional.get();
                }
            }
        }

        switch (schedulerType) {
            case WEEKLY:
                String dayAsString = configuration.getString(path + "day", "").toUpperCase();
                dayOfWeek = DAYS.getOrDefault(dayAsString, -1);
                if (dayOfWeek == -1) {
                    Logger.info("Impossible to find the day of " + path + " with weekly type", Logger.LogType.ERROR);
                    return null;
                }
                break;
            case MONTHLY:
                dayOfMonth = configuration.getInt(path + "day");
                break;
            case YEARLY:
                String monthAsString = configuration.getString(path + "month", "").toUpperCase();
                month = MONTHS.getOrDefault(monthAsString, -1);
                if (month == -1) {
                    Logger.info("Impossible to find the month of " + path + " with yearly type", Logger.LogType.ERROR);
                    return null;
                }
                dayOfMonth = configuration.getInt(path + "day");
                break;
            default:
                break;
        }

        if (schedulerType.isRepeatScheduler()) {
            if (previousExecution != 0) {
                initialDelay = (int) Math.max(0, schedulerType.convertToTimeUnit().toSeconds(period) - previousExecution);
                Logger.info("Previous execution of " + name + " found. Starting next execution in " + initialDelay + " seconds", Logger.LogType.INFO);
            } else {
                initialDelay = (int) schedulerType.convertToTimeUnit().toSeconds(initialDelay);
            }
            Instant lastExecution = Instant.now().minusSeconds(previousExecution);
            return new RepeatScheduler(plugin, name, schedulerType, lastExecution, initialDelay, saveTimer, period, minPlayer, commands, implementationName, implementationValues);
        }
        return new ClassicScheduler(plugin, name, schedulerType, dayOfMonth, dayOfWeek, month, hour, second, minute, minPlayer, commands, implementation, implementationName, implementationValues);
    }

    @Override
    public void save(Scheduler object, YamlConfiguration configuration, String path) {
        // Not use
    }
}

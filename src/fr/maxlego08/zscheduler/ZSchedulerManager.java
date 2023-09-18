package fr.maxlego08.zscheduler;

import fr.maxlego08.zscheduler.api.Implementation;
import fr.maxlego08.zscheduler.api.Scheduler;
import fr.maxlego08.zscheduler.api.SchedulerManager;
import fr.maxlego08.zscheduler.loader.SchedulerLoader;
import fr.maxlego08.zscheduler.zcore.logger.Logger;
import fr.maxlego08.zscheduler.zcore.utils.loader.Loader;
import fr.maxlego08.zscheduler.zcore.utils.storage.Persist;
import fr.maxlego08.zscheduler.zcore.utils.storage.Saveable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZSchedulerManager implements Saveable, SchedulerManager {

    private final List<Implementation> implementations = new ArrayList<>();
    private final List<Scheduler> schedulers = new ArrayList<>();
    private final SchedulerPlugin plugin;

    public ZSchedulerManager(SchedulerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void save(Persist persist) {
        schedulers.forEach(Scheduler::disable);
    }

    @Override
    public void load(Persist persist) {

        File file = new File(plugin.getDataFolder(), "schedulers.yml");
        if (!file.exists()) {
            plugin.saveResource("schedulers.yml", false);
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection configurationSection = configuration.getConfigurationSection("schedulers.");

        if (configurationSection == null) {
            Logger.info("Impossible to find the configuration section", Logger.LogType.ERROR);
            return;
        }

        for (String key : configurationSection.getKeys(false)) {
            Loader<Scheduler> loader = new SchedulerLoader(plugin, key);
            String path = "schedulers." + key + ".";
            Scheduler scheduler = loader.load(configuration, path);
            if (scheduler == null) continue;
            scheduler.start();
            schedulers.add(scheduler);
        }
    }

    @Override
    public boolean registerImplementation(Implementation implementation) {
        Optional<Implementation> optional = getImplementation(implementation.getName());
        if (!optional.isPresent()) {
            implementations.add(implementation);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Implementation> getImplementation(String name) {
        return implementations.stream().filter(e -> e.getName().equalsIgnoreCase(name)).findFirst();
    }
}

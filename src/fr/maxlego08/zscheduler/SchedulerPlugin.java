package fr.maxlego08.zscheduler;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.impl.ServerImplementation;
import fr.maxlego08.zscheduler.api.SchedulerManager;
import fr.maxlego08.zscheduler.command.commands.CommandSchedulers;
import fr.maxlego08.zscheduler.placeholder.LocalPlaceholder;
import fr.maxlego08.zscheduler.save.Config;
import fr.maxlego08.zscheduler.save.MessageLoader;
import fr.maxlego08.zscheduler.zcore.ZPlugin;
import fr.maxlego08.zscheduler.zcore.utils.plugins.Metrics;
import org.bukkit.plugin.ServicePriority;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class SchedulerPlugin extends ZPlugin {

    private ServerImplementation serverImplementation;
    private final ZSchedulerManager manager = new ZSchedulerManager(this);

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zscheduler");

        FoliaLib foliaLib = new FoliaLib(this);
        serverImplementation = foliaLib.getImpl();

        this.preEnable();

        this.registerCommand("zschedulers", new CommandSchedulers(this), "schedulers", "scheduler", "sch");
        this.getServer().getServicesManager().register(SchedulerManager.class, this.manager, this, ServicePriority.Highest);

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));
        this.addSave(this.manager);

        new Metrics(this, 19833);

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    public ZSchedulerManager getManager() {
        return manager;
    }

    public ServerImplementation getServerImplementation() {
        return serverImplementation;
    }
}

package fr.maxlego08.zscheduler;

import fr.maxlego08.zscheduler.api.SchedulerManager;
import fr.maxlego08.zscheduler.command.commands.CommandSchedulers;
import fr.maxlego08.zscheduler.implementations.ZKothImplementation;
import fr.maxlego08.zscheduler.placeholder.LocalPlaceholder;
import fr.maxlego08.zscheduler.save.Config;
import fr.maxlego08.zscheduler.save.MessageLoader;
import fr.maxlego08.zscheduler.zcore.ZPlugin;
import org.bukkit.plugin.ServicePriority;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class SchedulerPlugin extends ZPlugin {

    private final ZSchedulerManager manager = new ZSchedulerManager(this);

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zscheduler");

        this.preEnable();

        this.registerCommand("schedulers", new CommandSchedulers(this));
        this.getServer().getServicesManager().register(SchedulerManager.class, this.manager, this, ServicePriority.Highest);

        this.manager.registerImplementation(new ZKothImplementation());

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));
        this.addSave(this.manager);

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
}

package fr.maxlego08.zscheduler.command.commands;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.command.VCommand;
import fr.maxlego08.zscheduler.zcore.enums.Message;
import fr.maxlego08.zscheduler.zcore.enums.Permission;
import fr.maxlego08.zscheduler.zcore.utils.commands.CommandType;

public class CommandSchedulersReload extends VCommand {

    public CommandSchedulersReload(SchedulerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSCHEDULERS_RELOAD);
        this.addSubCommand("reload", "rl");
        this.setDescription(Message.DESCRIPTION_RELOAD);
    }

    @Override
    protected CommandType perform(SchedulerPlugin plugin) {

        plugin.getManager().save(plugin.getPersist());
        plugin.reloadFiles();
        message(sender, Message.RELOAD);

        return CommandType.SUCCESS;
    }

}

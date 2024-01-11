package fr.maxlego08.zscheduler.command.commands;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.api.Scheduler;
import fr.maxlego08.zscheduler.command.VCommand;
import fr.maxlego08.zscheduler.save.Config;
import fr.maxlego08.zscheduler.zcore.enums.Message;
import fr.maxlego08.zscheduler.zcore.enums.Permission;
import fr.maxlego08.zscheduler.zcore.utils.commands.CommandType;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommandSchedulersList extends VCommand {

    public CommandSchedulersList(SchedulerPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZSCHEDULERS_LIST);
        this.addSubCommand("list");
        this.setDescription(Message.DESCRIPTION_LIST);
    }

    @Override
    protected CommandType perform(SchedulerPlugin plugin) {

        List<Scheduler> schedulers = plugin.getManager().getSchedulers();
        message(sender, Message.SCHEDULER_LIST_HEADER);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Config.dateFormat);
        schedulers.forEach(e -> {
            message(sender, Message.SCHEDULER_LIST_LINE, "%name%", e.getName(), "%date%", simpleDateFormat.format(e.getCalendar().getTime()));
        });

        return CommandType.SUCCESS;
    }

}

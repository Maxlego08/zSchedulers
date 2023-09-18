package fr.maxlego08.zscheduler.command.commands;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.command.VCommand;
import fr.maxlego08.zscheduler.zcore.enums.Permission;
import fr.maxlego08.zscheduler.zcore.utils.commands.CommandType;

public class CommandSchedulers extends VCommand {

	public CommandSchedulers(SchedulerPlugin plugin) {
		super(plugin);
		this.addSubCommand("zschedulers", "scheduler", "sch");
		this.setPermission(Permission.SCHEDULERS_USE);
		this.addSubCommand(new CommandSchedulersReload(plugin));
	}

	@Override
	protected CommandType perform(SchedulerPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}

package fr.maxlego08.zscheduler.command.commands;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.command.VCommand;
import fr.maxlego08.zscheduler.zcore.enums.Permission;
import fr.maxlego08.zscheduler.zcore.utils.commands.CommandType;

public class CommandSchedulers extends VCommand {

	public CommandSchedulers(SchedulerPlugin plugin) {
		super(plugin);
		this.setPermission(Permission.ZSCHEDULERS_USE);
		this.addSubCommand(new CommandSchedulersReload(plugin));
		this.addSubCommand(new CommandSchedulersList(plugin));
		this.addSubCommand(new CommandSchedulersVersion(plugin));
	}

	@Override
	protected CommandType perform(SchedulerPlugin plugin) {
		syntaxMessage();
		return CommandType.SUCCESS;
	}

}

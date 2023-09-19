package fr.maxlego08.zscheduler.command.commands;

import fr.maxlego08.zscheduler.SchedulerPlugin;
import fr.maxlego08.zscheduler.command.VCommand;
import fr.maxlego08.zscheduler.zcore.enums.Message;
import fr.maxlego08.zscheduler.zcore.enums.Permission;
import fr.maxlego08.zscheduler.zcore.utils.commands.CommandType;

public class CommandSchedulersVersion extends VCommand {

    public CommandSchedulersVersion(SchedulerPlugin plugin) {
        super(plugin);
        this.addSubCommand("version", "ver", "?");
        this.setDescription(Message.DESCRIPTION_VERSION);
    }

    @Override
    protected CommandType perform(SchedulerPlugin plugin) {

        message(sender, "§aVersion du plugin§7: §2" + plugin.getDescription().getVersion());
        message(sender, "§aAuteur§7: §2Maxlego08");
        message(sender, "§aDiscord§7: §2http://discord.groupez.dev/");
        message(sender, "§aDownload€§7: §2https://groupez.dev/resources/303");
        message(sender, "§aSponsor§7: §chttps://serveur-minecraft-vote.fr/?ref=345");

        return CommandType.SUCCESS;
    }

}

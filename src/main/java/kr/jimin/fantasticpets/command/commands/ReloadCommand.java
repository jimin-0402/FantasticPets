package kr.jimin.fantasticpets.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Message;

public class ReloadCommand {

    public CommandAPICommand getReloadCommand() {
        return new CommandAPICommand("reload")
                .executes((sender, args) -> {
                    FantasticPetsPlugin.get().reloadConfigs();
                    Message.RELOAD.send(sender);
                });
    }
}

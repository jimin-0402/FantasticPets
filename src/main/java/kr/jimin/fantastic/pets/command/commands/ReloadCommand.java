package kr.jimin.fantastic.pets.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.config.Message;

public class ReloadCommand {

    public CommandAPICommand getReloadCommand() {
        return new CommandAPICommand("reload")
                .withPermission("fantasticpets.command.reload")
                .executes((sender, args) -> {
                    FantasticPetsPlugin.get().reloadConfigs();
                    Message.RELOAD.send(sender);
                });
    }
}

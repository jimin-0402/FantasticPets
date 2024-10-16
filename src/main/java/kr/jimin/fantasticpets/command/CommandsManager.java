package kr.jimin.fantasticpets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Message;

public class CommandsManager {
    private final FantasticPetsPlugin plugin;

    public CommandsManager(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadCommands() {
        new CommandAPICommand("fantasticpets")
                .withAliases("fp")
                .withSubcommands(
                        (new ReloadCommand(plugin)).getReloadCommand()
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                })
                .register();
    }
}

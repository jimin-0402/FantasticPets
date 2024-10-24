package kr.jimin.fantastic.pets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.command.commands.GiveItemCommand;
import kr.jimin.fantastic.pets.command.commands.ReloadCommand;
import kr.jimin.fantastic.pets.config.Message;

public class CommandsManager {
    private final FantasticPetsPlugin plugin;

    public CommandsManager(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadCommands() {
        new CommandAPICommand("fantasticpets")
                .withAliases("fp", "fpets")
                .withSubcommands(
                        (new PetCommand(plugin)).getPetCommand(),
                        (new ReloadCommand()).getReloadCommand(),
                        (new GiveItemCommand(plugin)).getGiveItemCommand()
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                })
                .register();
    }
}

package kr.jimin.fantasticpets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.command.pet.GivePetCommand;
import kr.jimin.fantasticpets.command.pet.GivePetItemCommand;
import kr.jimin.fantasticpets.command.pet.TakePetCommand;
import kr.jimin.fantasticpets.config.Message;

public class PetCommand {
    private final FantasticPetsPlugin plugin;

    public PetCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getPetCommand() {
        return new CommandAPICommand("pet")
                .withSubcommands(
                        (new GivePetItemCommand(plugin)).getGivePetItemCommand(),
                        (new GivePetCommand(plugin)).getGivePetCommand(),
                        (new TakePetCommand(plugin)).getTakePetCommand()
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                });
    }
}

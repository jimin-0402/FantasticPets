package kr.jimin.fantasticpets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.command.pet.PetGiveItemCommand;
import kr.jimin.fantasticpets.config.Message;

public class PetCommand {
    private final FantasticPetsPlugin plugin;

    public PetCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getPetCommand() {
        return new CommandAPICommand("pet")
                .withSubcommands(
                        (new PetGiveItemCommand(plugin)).getGivePetItemCommand()
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                });
    }
}

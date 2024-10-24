package kr.jimin.fantastic.pets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.command.pet.GivePetCommand;
import kr.jimin.fantastic.pets.command.pet.GivePetItemCommand;
import kr.jimin.fantastic.pets.command.pet.PetsListCommand;
import kr.jimin.fantastic.pets.command.pet.TakePetCommand;
import kr.jimin.fantastic.pets.config.Message;

public class PetCommand {
    private final FantasticPetsPlugin plugin;

    public PetCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getPetCommand() {
        return new CommandAPICommand("pet")
                .withPermission("fantasticpets.command.pet")
                .withSubcommands(
                        (new GivePetItemCommand(plugin)).getGivePetItemCommand(),
                        (new GivePetCommand(plugin)).getGivePetCommand(),
                        (new TakePetCommand(plugin)).getTakePetCommand(),
                        (new PetsListCommand(plugin)).getPetsListCommand()
                )
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                });
    }
}

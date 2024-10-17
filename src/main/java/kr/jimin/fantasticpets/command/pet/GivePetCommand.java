package kr.jimin.fantasticpets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.entity.Player;

public class GivePetCommand {
    private final FantasticPetsPlugin plugin;

    public GivePetCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getGivePetCommand() {
        return new CommandAPICommand("give")
                .withArguments(new PlayerArgument("player"))
                .withArguments(new StringArgument("pet").replaceSuggestions(ArgumentSuggestions.strings((context) -> PetsUtils.getAllPets().toArray(new String[0]))))
                .executes((sender, args) -> {
                    Player player = (Player) args.get("player");
                    if (player == null) {
                        sender.sendMessage("a");
                        return;
                    }
                    String petId = (String) args.get("pet");

                    String petName = PetsUtils.getPetNameFromId(petId);
                    if (petName == null) {
                        Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
                        return;
                    }

                    String petPerm = PetsUtils.getPetPermFromId(petId);
                    if (petPerm != null && player.hasPermission(petPerm)) {
                        Message.PET_HAS_TARGET.send(sender, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", player.getName()));
                        return;
                    }

                    PetsUtils.addPetsPermPlayer(player, petId);

                    Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", player.getName()));
                    if (!Message.COMMAND_GIVE_TARGET.toString().isEmpty()) {
                        Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", sender.getName()));
                    }
                });
    }
}

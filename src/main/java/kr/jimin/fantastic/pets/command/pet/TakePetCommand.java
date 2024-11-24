package kr.jimin.fantastic.pets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.config.Config;
import kr.jimin.fantastic.pets.config.Message;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import kr.jimin.fantastic.pets.util.logs.LogsManager;
import kr.jimin.fantastic.pets.util.pet.FantasticPetsUtils;
import kr.jimin.fantastic.pets.api.FantasticPetsAPI;
import org.bukkit.entity.Player;

public class TakePetCommand extends FantasticPetsAPI {
    private final FantasticPetsPlugin plugin;

    public TakePetCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getTakePetCommand() {
        return new CommandAPICommand("take")
                .withPermission("fantasticpets.command.pet.take")
                .withArguments(new PlayerArgument("player"))
                .withArguments(new StringArgument("petId").replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    Player player = (Player) info.previousArgs().get("player");
                    return getPlayerPets(player).toArray(new String[0]);
                })))
                .executes((sender, args) -> {
                    Player player = (Player) args.get("player");
                    if (player == null) {
                        return;
                    }
                    String petId = (String) args.get("petId");

                    String petName = getPetNameFromId(petId);
                    if (petName == null) {
                        Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
                        return;
                    }

                    String petPerm = getPetPermFromId(petId);
                    if (petPerm != null && !player.hasPermission(petPerm)) {
                        Message.PET_HAS_NOT.send(sender, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", player.getName()));
                        return;
                    }

                    FantasticPetsUtils.removePetsPermPlayer(player, petId);

                    Message.COMMAND_TAKE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", player.getName()));

                    if (!Message.COMMAND_TAKE_TARGET.toString().isEmpty()) {
                        Message.COMMAND_TAKE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", sender.getName()));
                    }

                    if (Config.SETTING_LOG.toBool()) {
                        new LogsManager(plugin).commandLog("take", player.getName(), player.getName(), petId + "/Pet", "1");
                    }
                });
    }
}

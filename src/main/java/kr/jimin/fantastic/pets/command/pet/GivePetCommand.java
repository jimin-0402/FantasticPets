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

public class GivePetCommand extends FantasticPetsAPI {
    private final FantasticPetsPlugin plugin;
    private final LogsManager logsManager;

    public GivePetCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
        this.logsManager = new LogsManager(plugin);
    }

    public CommandAPICommand getGivePetCommand() {
        return new CommandAPICommand("give")
                .withPermission("fantasticpets.command.pet.give")
                .withArguments(new PlayerArgument("player"))
                .withArguments(new StringArgument("pet").replaceSuggestions(ArgumentSuggestions.strings(context -> getAllPets().toArray(new String[0]))))
                .executes((sender, args) -> {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("This command can only be executed by a player.");
                        return;
                    }

                    Player player = (Player) args.get("player");
                    String petId = (String) args.get("pet");
                    String petName = getPetNameFromId(petId);

                    if (petName == null) {
                        Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
                        return;
                    }

                    String petPerm = getPetPermFromId(petId);
                    if (petPerm != null && player.hasPermission(petPerm)) {
                        Message.PET_HAS_TARGET.send(sender, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", player.getName()));
                        return;
                    }

                    FantasticPetsUtils.addPetsPermPlayer(player, petId);
                    Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", player.getName()));

                    if (!Message.COMMAND_GIVE_TARGET.toString().isEmpty()) {
                        Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", petName), MessagesUtils.tagResolver("player", sender.getName()));
                    }

                    if (Config.SETTING_LOG.toBool()) {
                        logsManager.commandLog("give", player.getName(), sender.getName(), petId + "/Pet", "1");
                    }
                });
    }
}

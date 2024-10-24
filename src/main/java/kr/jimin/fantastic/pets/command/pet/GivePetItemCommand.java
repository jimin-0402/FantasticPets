package kr.jimin.fantastic.pets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.config.Config;
import kr.jimin.fantastic.pets.util.logs.LogsManager;
import kr.jimin.fantastic.pets.util.pet.FantasticPetsUtils;
import kr.jimin.fantastic.pets.util.pet.PetsFileManager;
import org.bukkit.entity.Player;

public class GivePetItemCommand {
    private final FantasticPetsPlugin plugin;

    public GivePetItemCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getGivePetItemCommand() {
        return new CommandAPICommand("item")
                .withPermission("fantasticpets.command.pet.item")
                .withArguments(new PlayerArgument("player"))
                .withArguments(new StringArgument("item").replaceSuggestions(ArgumentSuggestions.strings(sender -> PetsFileManager.getPIList(plugin).toArray(new String[0]))))
                .withOptionalArguments(new IntegerArgument("amount").replaceSuggestions(ArgumentSuggestions.strings("1")))
                .executes((sender, args) -> {

                    Player player = (Player) args.get("player");
                    String id = (String) args.get("item");
                    int amount = ((Number) args.getOrDefault("amount", 1)).intValue();

                    FantasticPetsUtils.giveItem(plugin, sender, player, id, amount);
                    if (Config.SETTING_LOG.toBool()) {
                        new LogsManager(plugin).commandLog("give", player.getName(), player.getName(), id + "/Item", String.valueOf(amount));
                    }
                });
    }
}

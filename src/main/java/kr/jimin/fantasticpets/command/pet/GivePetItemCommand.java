package kr.jimin.fantasticpets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.util.logs.LogsManager;
import kr.jimin.fantasticpets.util.pet.FantasticPetsUtils;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
import org.bukkit.entity.Player;

public class GivePetItemCommand {
    private final FantasticPetsPlugin plugin;

    public GivePetItemCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getGivePetItemCommand() {
        return new CommandAPICommand("item")
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

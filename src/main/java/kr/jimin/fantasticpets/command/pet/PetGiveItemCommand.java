package kr.jimin.fantasticpets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
import org.bukkit.entity.Player;

public class PetGiveItemCommand {
    private final FantasticPetsPlugin plugin;

    public PetGiveItemCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getGivePetItemCommand() {
        return new CommandAPICommand("item")
                .withArguments(
                        new PlayerArgument("player"),
                        new StringArgument("item").replaceSuggestions(ArgumentSuggestions.strings(sender -> PetsFileManager.getPIList(plugin).toArray(new String[0]))),
                        new IntegerArgument("amount").setOptional(true).replaceSuggestions(ArgumentSuggestions.strings("1"))
                )
                .executes((sender, args) -> {

                    Player player = (Player) args.get("player");
                    String id = (String) args.get("item");
                    int amount = ((Number) args.getOrDefault("amount", 1)).intValue();

                    PetsFileManager.giveItem(plugin, sender, player, id, amount);
                });
    }
}

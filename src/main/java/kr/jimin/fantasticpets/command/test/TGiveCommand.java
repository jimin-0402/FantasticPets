package kr.jimin.fantasticpets.command.test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
import org.bukkit.entity.Player;

public class TGiveCommand {
    private final FantasticPetsPlugin plugin;

    public TGiveCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getPlayerPetsCommand() {
        return new CommandAPICommand("give")
                .withArguments(
                        new PlayerArgument("player"),
                        new StringArgument("item").replaceSuggestions(ArgumentSuggestions.strings(sender -> PetsFileManager.getPIList(plugin).toArray(new String[0]))),
                        new IntegerArgument("amount").setOptional(true).replaceSuggestions(ArgumentSuggestions.strings("1"))
                )
                .executes((sender, args) -> {

                    Player player = (Player) args.get("player");
                    String id = (String) args.get("item");
                    int amount = ((Number) args.getOrDefault("amount", 1)).intValue();

//                    ItemStack petsItem = PetsFileManager.loadPetItems(plugin, id);
/*

                    String inCategory = PetsUtils.getCategoryOfPet(id);
                    if (inCategory != null) {
                        player.sendMessage(inCategory);
                    }
*/

                    PetsFileManager.giveItem(plugin, sender, player, id, amount);
                });
    }
}


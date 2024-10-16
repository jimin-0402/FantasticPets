package kr.jimin.fantasticpets.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.util.item.ItemHandler;
import kr.jimin.fantasticpets.util.item.ItemUtils;
import org.bukkit.entity.Player;

public class GiveItemCommand {

    public CommandAPICommand getGiveItemCommand() {
        return new CommandAPICommand("item")
                .withArguments(
                        new PlayerArgument("player"),
                        new IntegerArgument("amount").setOptional(true).replaceSuggestions(ArgumentSuggestions.strings("1"))
                )
                .executes((sender, args) -> {
                    Player player = (Player) args.get("player");
                    int amount = ((Number) args.getOrDefault("amount", 1)).intValue();

                    if (player == null){
                        sender.sendMessage("a");
                        return;
                    }

                    ItemUtils customItem = ItemHandler.create(
                            Config.PET_ITEM_MATERIAL.toString(),
                            Config.PET_ITEM_NAME.toString(),
                            Config.PET_ITEM_LORE.toStringList(),
                            amount,
                            Config.PET_ITEM_MODEL_DATA.toInt()
                    );


                    if (customItem == null) {
                        return;
                    }
                    customItem.giveItem(player, amount);
                });
    }
}
package kr.jimin.fantasticpets.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.item.ItemHandler;
import kr.jimin.fantasticpets.util.item.ItemUtils;
import kr.jimin.fantasticpets.util.logs.LogsManager;
import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.entity.Player;

public class GiveItemCommand {
    private final FantasticPetsPlugin plugin;

    public GiveItemCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

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
                        return;
                    }

                    ItemUtils customItem = ItemHandler.create(
                            Config.PET_ITEM_MATERIAL.toString(),
                            Config.PET_ITEM_NAME.toString(),
                            Config.PET_ITEM_LORE.toStringList(),
                            amount,
                            Config.PET_ITEM_MODEL_DATA.toInt()
                    );


                    if (customItem == null) return;

                    customItem.giveItem(player, amount);
                    Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("player", player.name()), MessagesUtils.tagResolver("pet-name", Config.PET_ITEM_NAME.toComponent()));
                    Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("player", sender.name()), MessagesUtils.tagResolver("pet-name", Config.PET_ITEM_NAME.toComponent()));

                    if (Config.SETTING_LOG.toBool()) {
                        new LogsManager(plugin).commandLog("give", player.getName(), player.getName(), "MainItem");
                    }
                });
    }
}
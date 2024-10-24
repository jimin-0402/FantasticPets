package kr.jimin.fantastic.pets.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.config.Config;
import kr.jimin.fantastic.pets.config.Message;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import kr.jimin.fantastic.pets.util.item.ItemHandler;
import kr.jimin.fantastic.pets.util.item.ItemUtils;
import kr.jimin.fantastic.pets.util.logs.LogsManager;
import org.bukkit.entity.Player;

public class GiveItemCommand {
    private final FantasticPetsPlugin plugin;

    public GiveItemCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getGiveItemCommand() {
        return new CommandAPICommand("item")
                .withPermission("fantasticpets.command.item")
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

                    if (customItem == null) {
                        Message.NOT_ITEM.send(sender, MessagesUtils.tagResolver("pet-item", Config.PET_ITEM_NAME.toComponent()));
                        return;
                    }

                    customItem.giveItem(player, amount);
                    Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("player", player.name()), MessagesUtils.tagResolver("pet-name", Config.PET_ITEM_NAME.toComponent()));
                    Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("player", sender.name()), MessagesUtils.tagResolver("pet-name", Config.PET_ITEM_NAME.toComponent()));

                    if (Config.SETTING_LOG.toBool()) {
                        new LogsManager(plugin).commandLog("give", player.getName(), player.getName(), "MainItem", String.valueOf(amount));
                    }
                });
    }
}
package kr.jimin.fantasticpets.command;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;

public class ReloadCommand {

    private final FantasticPetsPlugin plugin;

    public ReloadCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    CommandAPICommand getReloadCommand() {
        return new CommandAPICommand("reload")
                .withAliases("rl")
                .executes((sender, args) -> {
                    FantasticPetsPlugin.get().reloadConfigs();
                    PetsFileManager.reloadPetItems(plugin);
                    Message.RELOAD.send(sender);
                });
    }
}

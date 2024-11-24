package kr.jimin.fantastic.pets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.config.Message;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import kr.jimin.fantastic.pets.util.pet.PetsFileManager;
import kr.jimin.fantastic.pets.api.FantasticPetsAPI;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PetsListCommand extends FantasticPetsAPI {
    private final FantasticPetsPlugin plugin;

    public PetsListCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getPetsListCommand() {
        return new CommandAPICommand("list")
                .withPermission("fantasticpets.command.pet.list")
                .withSubcommands(getMCPetsListCommand(), getFantasticPetsListCommand())
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                });
    }

    private CommandAPICommand getMCPetsListCommand() {
        return new CommandAPICommand("mcpets")
                .withPermission("fantasticpets.command.pet.list.mcpets")
                .executes((sender, args) -> {
                    getPetList(sender, "mcpets");
                });
    }

    private CommandAPICommand getFantasticPetsListCommand() {
        return new CommandAPICommand("fantasticpets")
                .withPermission("fantasticpets.command.pet.list.fantasticpets")
                .executes((sender, args) -> {
                    getPetList(sender, "fantasticpets");
                });
    }

    private List<String> getPetList(CommandSender player, String type) {
        List<String> petList = new ArrayList<>();

        if ("mcpets".equalsIgnoreCase(type)) {
            petList.addAll(getAllPets());
            Message.PET_LIST_TYPE_FORMAT.send(player, MessagesUtils.tagResolver("plugin-type", "MCPets"));
        } else if ("fantasticpets".equalsIgnoreCase(type)) {
            petList.addAll(PetsFileManager.getPIList(plugin));
            Message.PET_LIST_TYPE_FORMAT.send(player, MessagesUtils.tagResolver("plugin-type", "FantasticPets"));
        }

        for (int i = 0; i < petList.size(); i++) {
            String petId = petList.get(i);
            String petName = getPetNameFromId(petId);

            Message.PET_LIST_PET_FORMAT.send(player, MessagesUtils.tagResolver("number-index", String.valueOf(i + 1)),
                    MessagesUtils.tagResolver("pet-id", petId),
                    MessagesUtils.tagResolver("pet-name", petName));
        }

        return petList;
    }
}

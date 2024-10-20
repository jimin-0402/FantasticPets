package kr.jimin.fantasticpets.command.pet;

import dev.jorel.commandapi.CommandAPICommand;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.logs.LogsManager;
import kr.jimin.fantasticpets.util.pet.FantasticPetsUtils;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PetsListCommand {
    private final FantasticPetsPlugin plugin;

    public PetsListCommand(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandAPICommand getPetsListCommand() {
        return new CommandAPICommand("list")
                .withSubcommands(getMCPetsListCommand(), getFantasticPetsListCommand())
                .executes((sender, args) -> {
                    Message.COMMAND_HELP.send(sender);
                });
    }

    private CommandAPICommand getMCPetsListCommand() {
        return new CommandAPICommand("mcpets")
                .executes((sender, args) -> {
                    getPetList(sender, "mcpets");
                });
    }

    private CommandAPICommand getFantasticPetsListCommand() {
        return new CommandAPICommand("fantasticpets")
                .executes((sender, args) -> {
                    getPetList(sender, "fantasticpets");
                });
    }

    private List<String> getPetList(CommandSender player, String type) {
        List<String> petList = new ArrayList<>();

        if ("mcpets".equalsIgnoreCase(type)) {
            petList.addAll(PetsUtils.getAllPets());
            Message.PET_LIST_TYPE_FORMAT.send(player, MessagesUtils.tagResolver("plugin-type", "MCPets"));
        } else if ("fantasticpets".equalsIgnoreCase(type)) {
            petList.addAll(PetsFileManager.getPIList(plugin));
            Message.PET_LIST_TYPE_FORMAT.send(player, MessagesUtils.tagResolver("plugin-type", "FantasticPets"));
        }

        for (int i = 0; i < petList.size(); i++) {
            String petId = petList.get(i);
            String petName = PetsUtils.getPetNameFromId(petId); // 펫 이름 가져오기

            Message.PET_LIST_PET_FORMAT.send(player, MessagesUtils.tagResolver("number-index", String.valueOf(i + 1)),
                    MessagesUtils.tagResolver("pet-id", petId),
                    MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)));
        }

        return petList;
    }

}

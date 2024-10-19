package kr.jimin.fantasticpets.util.pet;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.LuckPermsUtils;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.SoundsUtils;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FantasticPetsUtils {

    public static void randomGetPet(FantasticPetsPlugin plugin, Player player, ItemStack item) {
        List<String> playerPets = PetsUtils.getPlayerPets(player);

        if (PetsUtils.isAllPets(player)) {
            SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
            return;
        }

        boolean duplication = Config.PET_DUPLICATION.toBool();
        List<String> enabledPets = PetsFileManager.getPIList(plugin);
        String petId = null;

        petId = PetsUtils.getRandomPetId(plugin, playerPets, enabledPets, duplication);
        if (petId == null) {
            Message.PET_WITHOUT_MAIN.send(player);
            SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
            return;
        }

        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);
        if (petItem == null) {
            return;
        }

        player.getInventory().addItem(petItem);
        player.getInventory().removeItem(item);

        getCategoryMessage(player, petId);
        SoundsUtils.playSound(player, Config.SOUND_SUCCESS.toStringList());
    }

    public static void getCategoryMessage(Player player, String petId) {
        String petName = PetsUtils.getPetNameFromId(petId);
        Component categoryComponent = PetsUtils.getCategory(petId);

        if (Config.PET_USE_CATEGORY.toBool()) {
            if (categoryComponent == null) {
                Message.PET_ACQUIRED.send(player, MessagesUtils.tagResolver("pet-name", petName));
                return;
            }
            Message.PET_ACQUIRED_CATEGORY.send(player,
                    MessagesUtils.tagResolver("pet-name", petName),
                    MessagesUtils.tagResolver("category-name", categoryComponent),
                    MessagesUtils.tagResolver("category-prefix", MessagesUtils.processMessage(Message.PET_CATEGORY_PREFIX.toString())));
        } else {
            Message.PET_ACQUIRED.send(player, MessagesUtils.tagResolver("pet-name", petName));
        }
    }

    public static void addPetsPermPlayer(Player player, String petId) {
        User user = LuckPermsUtils.getLuckPermsUser(player);
        String petPerm = PetsUtils.getPetPermFromId(petId);
        if (petPerm != null) {
            Node node = Node.builder(petPerm).build();
            user.data().add(node);
        }
        LuckPermsProvider.get().getUserManager().saveUser(user);
    }

    public static void removePetsPermPlayer(Player player, String petId) {
        User user = LuckPermsUtils.getLuckPermsUser(player);
        String petPerm = PetsUtils.getPetPermFromId(petId);
        if (petPerm != null) {
            Node node = Node.builder(petPerm).build();
            user.data().remove(node);
        }
        LuckPermsProvider.get().getUserManager().saveUser(user);
    }

    public static void giveItem(FantasticPetsPlugin plugin, CommandSender sender, Player player, String petId, int amount) {
        ItemStack iS = PetsFileManager.loadPetItems(plugin, petId);

        if (iS == null) {
            Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
            return;
        }

        iS.setAmount(amount);
        player.getInventory().addItem(iS);
        Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", player.displayName()));
        Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", sender.name()));
    }
}

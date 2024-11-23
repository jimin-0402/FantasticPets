package kr.jimin.fantastic.pets.util.pet;

import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.config.Config;
import kr.jimin.fantastic.pets.config.Message;
import kr.jimin.fantastic.pets.util.LuckPermsUtils;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import kr.jimin.fantastic.pets.util.SoundsUtils;
import kr.jimin.fantastic.pets.util.logs.LogsManager;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class FantasticPetsUtils {

    public static void randomGetPet(FantasticPetsPlugin plugin, Player player, ItemStack item) {
        List<String> playerPets = PetsUtils.getPlayerPets(player);

        if (isAllPets(player)) {
            SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
            return;
        }

        boolean useWeight = Config.PET_USE_CHANCE.toBool();

        String petId;

        if (!useWeight) {
            List<String> availablePets = PetsFileManager.getPIList(plugin);

            if (availablePets.isEmpty()) {
                Message.PET_WITHOUT_MAIN.send(player);
                SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
                return;
            }

            petId = availablePets.get(new Random().nextInt(availablePets.size()));
        } else {
            List<String> weightedPets = PetsFileManager.getWeightedPetList(plugin, playerPets);

            if (weightedPets.isEmpty()) {
                Message.PET_WITHOUT_MAIN.send(player);
                SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
                return;
            }

            petId = weightedPets.get(new Random().nextInt(weightedPets.size()));
        }

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

        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem.isSimilar(item)) {
            int newAmount = handItem.getAmount() - 1;
            if (newAmount > 0) {
                handItem.setAmount(newAmount);
            } else {
                player.getInventory().setItemInMainHand(null);
            }
        }

        getCategoryMessage(plugin, player, petId, true);
        SoundsUtils.playSound(player, Config.SOUND_SUCCESS.toStringList());
        new LogsManager(plugin).logUser(player.getName(), petId);
    }

    public static void getCategoryMessage(FantasticPetsPlugin plugin, Player player, String petId, boolean sendChance) {
        String petName = PetsUtils.getPetNameFromId(petId);
        Component categoryComponent = PetsUtils.getCategoryNameByPetId(petId);

        boolean useChance = Config.PET_USE_CHANCE.toBool();
        double chance = useChance ? PetsFileManager.getChance(plugin, petId) : 0.0;

        if (Config.PET_USE_CATEGORY.toBool() && categoryComponent != null) {
            if (sendChance && useChance) {
                Message.PET_ACQUIRED_CATEGORY_CHANCE.send(player,
                        MessagesUtils.tagResolver("pet-name", petName),
                        MessagesUtils.tagResolver("category-name", categoryComponent),
                        MessagesUtils.tagResolver("category-prefix", MessagesUtils.processMessage(Message.PET_CATEGORY_PREFIX.toString())),
                        MessagesUtils.tagResolver("chance", String.valueOf(chance)));
            } else {
                Message.PET_ACQUIRED_CATEGORY.send(player,
                        MessagesUtils.tagResolver("pet-name", petName),
                        MessagesUtils.tagResolver("category-name", categoryComponent),
                        MessagesUtils.tagResolver("category-prefix", MessagesUtils.processMessage(Message.PET_CATEGORY_PREFIX.toString())));
            }
        } else {
            if (sendChance && useChance) {
                Message.PET_ACQUIRED_CHANCE.send(player,
                        MessagesUtils.tagResolver("pet-name", petName),
                        MessagesUtils.tagResolver("chance", String.valueOf(chance)));
            } else {
                Message.PET_ACQUIRED.send(player, MessagesUtils.tagResolver("pet-name", petName));
            }
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
        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);

        if (petItem == null) {
            Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
            return;
        }

        petItem.setAmount(amount);
        player.getInventory().addItem(petItem);
        Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", player.displayName()));
        Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", sender.name()));
    }

    public static boolean isHasPlayerPet(Player player, String id, boolean message) {
        List<String> playerPets = PetsUtils.getPlayerPets(player);
        String petName = PetsUtils.getPetNameFromId(id);
        if (playerPets.contains(id)) {
            if (message) {
                Message.PET_HAS.send(player, MessagesUtils.tagResolver("pet-name", petName));
                SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
            }
            return true;
        }
        return false;
    }

    public static boolean isAllPets(Player player) {
        List<String> playerPets = PetsUtils.getPlayerPets(player);
        List<String> allPets = PetsFileManager.getPIList(FantasticPetsPlugin.get());

        if (allPets.isEmpty()) {
            return false;
        }

        if (playerPets.containsAll(allPets)) {
            Message.PET_HAS_ALL.send(player);
            return true;
        }

        return false;
    }

}

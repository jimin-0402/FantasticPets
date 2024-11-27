package kr.jimin.fantastic.pets.util.pet;

import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.api.FantasticPetsAPI;
import kr.jimin.fantastic.pets.config.Config;
import kr.jimin.fantastic.pets.config.Message;
import kr.jimin.fantastic.pets.util.LuckPermsUtils;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import kr.jimin.fantastic.pets.util.SoundsUtils;
import kr.jimin.fantastic.pets.util.logs.Logs;
import kr.jimin.fantastic.pets.util.logs.LogsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class FantasticPetsUtils {

    private static final FantasticPetsAPI petsAPI = new FantasticPetsAPI();

    public static void randomGetPet(FantasticPetsPlugin plugin, Player player, ItemStack item) {
        List<String> playerPets = petsAPI.getPlayerPets(player);

        if (isAllPets(player)) {
            SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
            return;
        }

        String petId = getRandomPetId(plugin, player, playerPets);
        if (petId == null) {
            return;
        }

        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);
        if (petItem == null) {
            return;
        }

        addPetToInventory(player, item, petItem);
        getCategoryMessage(plugin, player, petId, true);
        SoundsUtils.playSound(player, Config.SOUND_SUCCESS.toStringList());
        new LogsManager(plugin).logUser(player.getName(), petId);
    }

    private static String getRandomPetId(FantasticPetsPlugin plugin, Player player, List<String> playerPets) {
        boolean useWeight = Config.PET_USE_CHANCE.toBool();
        List<String> petList = useWeight ? PetsFileManager.getWeightedPetList(plugin, playerPets) : PetsFileManager.getPIList(plugin);

        if (petList.isEmpty()) {
            Message.PET_WITHOUT_MAIN.send(player);
            SoundsUtils.playSound(player, Config.SOUND_FAIL.toStringList());
            return null;
        }

        return petList.get(new Random().nextInt(petList.size()));
    }

    private static void addPetToInventory(Player player, ItemStack item, ItemStack petItem) {
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
    }

    public static void getCategoryMessage(FantasticPetsPlugin plugin, Player player, String petId, boolean sendChance) {
        String petName = petsAPI.getPetNameFromId(petId);
        String categoryId = petsAPI.getCategoryOfPet(petId);
        String categoryComponent = petsAPI.getCategoryNameById(categoryId);
        String categoryPrefix = Message.PET_CATEGORY_PREFIX.toString();

        String categoryName = (categoryId != null && !categoryId.isEmpty() && categoryComponent != null) ?
                categoryComponent : "";

        if (categoryId == null || categoryId.isEmpty()) {
            if(Config.DEBUG.toBool()){ Logs.logWarning("No category found for petId " + petId); }
        }

        double chance = Config.PET_USE_CHANCE.toBool() ? PetsFileManager.getChance(plugin, petId) : 0.0;

        String titleMessage = replacePlaceholders(Message.TITLE_MAIN.toString(), petName,
                categoryName.isEmpty() ? "" : categoryPrefix, categoryName, chance);
        String subtitleMessage = replacePlaceholders(Message.TITLE_SUB.toString(), petName,
                categoryName.isEmpty() ? "" : categoryPrefix, categoryName, chance);

        Component titleComponent = MessagesUtils.processMessage(titleMessage);
        Component subtitleComponent = Config.PET_USE_CHANCE.toBool() ? MessagesUtils.processMessage(subtitleMessage) : Component.empty();

        Title.Times times = Title.Times.times(
                Duration.ofMillis(Config.Title_FADE_IN.toInt()),
                Duration.ofMillis(Config.Title_STAY.toInt()),
                Duration.ofMillis(Config.Title_FADE_OUT.toInt())
        );

        Title title = Title.title(titleComponent, subtitleComponent, times);
        handleMessageDisplay(player, title, petName, categoryComponent, chance, sendChance);
    }

    private static void handleMessageDisplay(Player player, Title title, String petName, String categoryComponent, double chance, boolean sendChance) {
        String messageType = Config.SETTING_MESSAGE_TYPE.toString();

        switch (messageType) {
            case "TITLE":
                player.showTitle(title);
                break;

            case "CHAT":
                sendPetMessage(player, petName, categoryComponent, chance, sendChance);
                break;

            case "DUAL":
                player.showTitle(title);
                sendPetMessage(player, petName, categoryComponent, chance, sendChance);
                break;

            case "NONE":
            default:
                break;
        }
    }

    private static String replacePlaceholders(String template, String petName, String categoryPrefix, String categoryName, double chance) {
        return template
                .replace("<category-name>", categoryName)
                .replace("<category-prefix>", categoryPrefix)
                .replace("<pet-name>", petName)
                .replace("<chance>", String.valueOf(chance));
    }

    private static void sendPetMessage(Player player, String petName, String categoryComponent, double chance, boolean sendChance) {
        if (Config.PET_USE_CATEGORY.toBool() && categoryComponent != null) {
            if (sendChance) {
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
            if (sendChance) {
                Message.PET_ACQUIRED_CHANCE.send(player,
                        MessagesUtils.tagResolver("pet-name", petName),
                        MessagesUtils.tagResolver("chance", String.valueOf(chance)));
            } else {
                Message.PET_ACQUIRED.send(player, MessagesUtils.tagResolver("pet-name", petName));
            }
        }
    }

    public static void addPetsPermPlayer(Player player, String petId) {
        updatePetPermissions(player, petId, true);
    }

    public static void removePetsPermPlayer(Player player, String petId) {
        updatePetPermissions(player, petId, false);
    }

    private static void updatePetPermissions(Player player, String petId, boolean add) {
        User user = LuckPermsUtils.getLuckPermsUser(player);
        String petPerm = petsAPI.getPetPermFromId(petId);
        if (petPerm != null) {
            Node node = Node.builder(petPerm).build();
            if (add) {
                user.data().add(node);
            } else {
                user.data().remove(node);
            }
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }
    }

    public static void giveItem(FantasticPetsPlugin plugin, CommandSender sender, Player player, String petId, int amount) {
        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);

        if (petItem == null) {
            Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
            return;
        }

        petItem.setAmount(amount);
        player.getInventory().addItem(petItem);
        Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", petsAPI.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", player.displayName()));
        Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", petsAPI.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", sender.getName()));
    }

    public static boolean isHasPlayerPet(Player player, String id, boolean message) {
        List<String> playerPets = petsAPI.getPlayerPets(player);
        String petName = petsAPI.getPetNameFromId(id);
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
        List<String> playerPets = petsAPI.getPlayerPets(player);
        List<String> allPets = PetsFileManager.getPIList(FantasticPetsPlugin.get());

        if (playerPets.containsAll(allPets)) {
            Message.PET_HAS_ALL.send(player);
            return true;
        }

        return false;
    }
}

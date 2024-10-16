package kr.jimin.fantasticpets.util.pet;

import fr.nocsy.mcpets.api.MCPetsAPI;
import fr.nocsy.mcpets.data.Category;
import fr.nocsy.mcpets.data.Pet;
import fr.nocsy.mcpets.data.config.PetConfig;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.LuckPermsUtils;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.SoundsUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PetsUtils {

    private static List<String> getFormattedPetList(String petList) {
        String[] petsArray = petList
                .replace("AlmPet", "")
                .replace(";", "")
                .replace("[", "")
                .replace("]", "")
                .split(",");

        List<String> pets = new ArrayList<>();
        for (String pet : petsArray) {
            String trimmedPet = pet.trim();
            if (!trimmedPet.isEmpty()) {
                pets.add(trimmedPet);
            }
        }
        return pets;
    }

    public static String getRandomPetId() {
        String petList = MCPetsAPI.getObjectPets().toString();
        List<String> pets = getFormattedPetList(petList);
        return pets.isEmpty() ? null : pets.get((int) (Math.random() * pets.size()));
    }

    public static List<String> getAllPets() {
        String allPets = MCPetsAPI.getObjectPets().toString();
        return getFormattedPetList(allPets);
    }

    public static List<String> getPlayerPets(Player player) {
        String petList = MCPetsAPI.getAvailablePets(player).toString();
        return getFormattedPetList(petList);
    }

    private static FileConfiguration getPetConfigFromId(String id) {
        return PetConfig.getConfig(id).getConfig();
    }

    public static String getPetNameFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getString("Icon.Name") : null;
    }

    public static String getPetMaterialFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getString("Icon.Material") : "STONE";
    }

    public static int getPetCustomModelDataFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getInt("Icon.CustomModelData") : -1;
    }

    public static List<String> getPetLoreFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getStringList("Icon.Description") : Collections.emptyList();
    }

    public static String getPetPermFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getString("Permission") : null;
    }

    public static String getCategoryOfPet(String petId) {
        List<Category> allCategories = Category.getCategories();
        for (Category category : allCategories) {
            if (category.getPets().stream().anyMatch(pet -> pet.getId().equals(petId))) {
                return category.getId();
            }
        }
        return null;
    }

    public static List<String> getPetsInCategory(String categoryId) {
        Category category = Category.getFromId(categoryId);
        return category != null ? category.getPets().stream().map(Pet::getId).toList() : new ArrayList<>();
    }

    public static String getCategoryNameById(String categoryId) {
        Category category = Category.getFromId(categoryId);
        return category != null ? category.getDisplayName() : null;
    }

    public static boolean isHasPlayerPet(Player player, String id, boolean message) {
        List<String> playerPets = getPlayerPets(player);
        String petName = getPetNameFromId(id);
        if (playerPets.contains(id)) {
            if (message) {
                Message.PET_HAS.send(player, MessagesUtils.tagResolver("pet-name", petName));
            }
            Config.SOUND_FAIL.toStringList().forEach(soundConfig -> {
                List<String> soundData = List.of(soundConfig.split(","));
                SoundsUtils.playSound(player, soundData);
            });

            return true;
        }
        return false;
    }

    public static boolean isAllPets(Player player) {
        List<String> playerPets = getPlayerPets(player);
        List<String> allPets = getAllPets();
        if (playerPets.containsAll(allPets)) {
            Message.PET_HAS_ALL.send(player);
            return true;
        }
        return false;
    }

    public static void randomGetPet(FantasticPetsPlugin plugin, Player player, ItemStack item) {
        List<String> playerPets = getPlayerPets(player);
        List<String> allPets = getAllPets();

        // 모든 펫을 이미 보유하고 있는지 확인
        if (playerPets.containsAll(allPets)) {
            Message.PET_HAS_ALL.send(player);
            return;
        }

        boolean duplication = Config.PET_DUPLICATION.toBool();
        List<String> enabledPets = PetsFileManager.getPIList(plugin);
        int registeredPets = enabledPets.size();

        String petId = null;

        if (duplication) {
            for (int attempt = 0; attempt < registeredPets; attempt++) {
                petId = PetsFileManager.getRandomItemsID(plugin);
                if (petId != null && !playerPets.contains(petId) && enabledPets.contains(petId)) {
                    break;
                }
            }
        } else {
            petId = PetsFileManager.getRandomItemsID(plugin);
            if (petId != null && !enabledPets.contains(petId)) {
                player.sendMessage("Selected pet is not enabled.");
                return;
            }
        }

        if (petId == null || (duplication && playerPets.contains(petId))) {
            player.sendMessage("No valid pet ID found.");
            return;
        }

        player.sendMessage("abc4"); // 이 메시지가 출력되어야 함

        String petName = getPetNameFromId(petId);
        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);

        if (petItem == null) {
            player.sendMessage("Pet item is null."); // 디버깅 메시지 추가
            return;
        }

        player.getInventory().addItem(petItem);
        player.getInventory().removeItem(item);

        Component isCategory = getCategory(petId);

        Message.PET_ACQUIRED.send(player,
                MessagesUtils.tagResolver("pet-name", petName),
                MessagesUtils.tagResolver("category-name", getCategory(petId)),
                MessagesUtils.tagResolver("category-prefix", MessagesUtils.processMessage(Message.PET_CATEGORY_PREFIX.toString()))
        );

        Config.SOUND_SUCCESS.toStringList().forEach(soundConfig -> {
            List<String> soundData = List.of(soundConfig.split(","));
            SoundsUtils.playSound(player, soundData);
        });
    }



    public static void addPetsPermPlayer(Player player, String petId) {
        User user = LuckPermsUtils.getLuckPermsUser(player);
        String petPerm = getPetPermFromId(petId);
        if (petPerm != null) {
            Node node = Node.builder(petPerm).build();
            user.data().add(node);
        }
        LuckPermsProvider.get().getUserManager().saveUser(user);
    }

    public static void removePetsPermPlayer(Player player, String petId) {
        User user = LuckPermsUtils.getLuckPermsUser(player);
        String petPerm = getPetPermFromId(petId);
        if (petPerm != null) {
            Node node = Node.builder(petPerm).build();
            user.data().remove(node);
        }
        LuckPermsProvider.get().getUserManager().saveUser(user);
    }

    public static Component getCategory(String petId) {
        String petCategoryId = getCategoryOfPet(petId);
        String petCategoryName = getCategoryNameById(petCategoryId);
        assert petCategoryName != null;
        return MessagesUtils.processMessage(petCategoryName);
    }
}

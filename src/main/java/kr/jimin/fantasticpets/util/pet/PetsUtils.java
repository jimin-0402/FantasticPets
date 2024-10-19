package kr.jimin.fantasticpets.util.pet;

import fr.nocsy.mcpets.api.MCPetsAPI;
import fr.nocsy.mcpets.data.Category;
import fr.nocsy.mcpets.data.Pet;
import fr.nocsy.mcpets.data.config.PetConfig;
import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.SoundsUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
        PetConfig petConfig = PetConfig.getConfig(id);
        if (petConfig == null) return null;
        return petConfig.getConfig();
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

    public static Component getCategory(String petId) {
        String petCategoryId = getCategoryOfPet(petId);
        if (petCategoryId == null || petCategoryId.isEmpty()) return null;

        String petCategoryName = getCategoryNameById(petCategoryId);
        if (petCategoryName == null) return null;

        return MessagesUtils.processMessage(petCategoryName);
    }
}

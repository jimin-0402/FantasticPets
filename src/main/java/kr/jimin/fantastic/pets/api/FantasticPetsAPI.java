package kr.jimin.fantastic.pets.api;

import fr.nocsy.mcpets.api.MCPetsAPI;
import fr.nocsy.mcpets.data.Category;
import fr.nocsy.mcpets.data.Pet;
import fr.nocsy.mcpets.data.config.PetConfig;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * FantasticPetsAPI provides functionalities related to pets.
 * This API allows access to and manipulation of pet information.
 */
public class FantasticPetsAPI {

    private Random random;

    public FantasticPetsAPI() {
        this.random = new Random();
    }

    /**
     * Returns a random pet ID.
     *
     * @return Random pet ID
     */
    public String getRandomPetId() {
        List<String> pets = getFormattedPetList(MCPetsAPI.getObjectPets().toString());
        return pets.isEmpty() ? null : pets.get(random.nextInt(pets.size()));
    }

    /**
     * Returns a list of all pets.
     *
     * @return List of all pets
     */
    public List<String> getAllPets() {
        return getFormattedPetList(MCPetsAPI.getObjectPets().toString());
    }

    /**
     * Returns a list of pets owned by a specific player.
     *
     * @param player Player whose pets are to be retrieved
     * @return List of pets owned by the specific player
     */
    public List<String> getPlayerPets(Player player) {
        return getFormattedPetList(MCPetsAPI.getAvailablePets(player).toString());
    }

    /**
     * Returns the pet name from a given pet ID.
     *
     * @param id Pet ID to retrieve the name from
     * @return Pet name
     */
    public String getPetNameFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getString("Icon.Name") : null;
    }

    /**
     * Returns the material of the pet from a given pet ID.
     *
     * @param id Pet ID to retrieve the material from
     * @return Pet material
     */
    public String getPetMaterialFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getString("Icon.Material") : "STONE";
    }

    /**
     * Returns the custom model data of the pet from a given pet ID.
     *
     * @param id Pet ID to retrieve the custom model data from
     * @return Pet's custom model data
     */
    public int getPetCustomModelDataFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getInt("Icon.CustomModelData") : -1;
    }

    /**
     * Returns the lore of the pet from a given pet ID.
     *
     * @param id Pet ID to retrieve the lore from
     * @return List of pet lore
     */
    public List<String> getPetLoreFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getStringList("Icon.Description") : Collections.emptyList();
    }

    /**
     * Returns the permission of the pet from a given pet ID.
     *
     * @param id Pet ID to retrieve the permission from
     * @return Pet permission
     */
    public String getPetPermFromId(String id) {
        FileConfiguration config = getPetConfigFromId(id);
        return config != null ? config.getString("Permission") : null;
    }

    /**
     * Returns the category of the pet from a given pet ID.
     *
     * @param petId Pet ID to retrieve the category from
     * @return Pet category
     */
    public String getCategoryOfPet(String petId) {
        List<Category> allCategories = Category.getCategories();
        for (Category category : allCategories) {
            if (category.getPets().stream().anyMatch(pet -> pet.getId().equals(petId))) {
                return category.getId();
            }
        }
        return "";
    }

    /**
     * Returns a list of pets in a given category ID.
     *
     * @param categoryId Category ID to retrieve the pet list from
     * @return List of pets in the given category
     */
    public List<String> getPetsInCategory(String categoryId) {
        Category category = Category.getFromId(categoryId);
        return category != null ? category.getPets().stream().map(Pet::getId).toList() : new ArrayList<>();
    }

    /**
     * Returns the category name by category ID.
     *
     * @param categoryId Category ID to retrieve the name from
     * @return Category name
     */
    public String getCategoryNameById(String categoryId) {
        Category category = Category.getFromId(categoryId);
        return category != null ? category.getDisplayName() : null;
    }

    /**
     * Returns the category name by pet ID.
     *
     * @param petId Pet ID to retrieve the category name from
     * @return Pet's category name
     */
    public Component getCategoryNameByPetId(String petId) {
        String petCategoryId = getCategoryOfPet(petId);
        if (petCategoryId == null || petCategoryId.isEmpty()) return null;

        String petCategoryName = getCategoryNameById(petCategoryId);
        if (petCategoryName == null) return null;

        return MessagesUtils.processMessage(petCategoryName);
    }

    // Internal method: Helper method to format pet list
    private List<String> getFormattedPetList(String petList) {
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

    // Internal method: Helper method to get configuration from pet ID
    private FileConfiguration getPetConfigFromId(String id) {
        PetConfig petConfig = PetConfig.getConfig(id);
        return petConfig != null ? petConfig.getConfig() : null;
    }
}

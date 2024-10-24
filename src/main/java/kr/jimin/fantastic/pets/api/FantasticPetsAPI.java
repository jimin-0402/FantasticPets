package kr.jimin.fantastic.pets.api;

import kr.jimin.fantastic.pets.util.pet.PetsUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class FantasticPetsAPI {

    // 랜덤 펫 ID를 반환합니다.
    // Returns a random pet ID.
    public String getRandomPetId() {
        return PetsUtils.getRandomPetId();
    }

    // 모든 펫의 리스트를 반환합니다.
    // Returns a list of all pets.
    public List<String> getAllPets() {
        return PetsUtils.getAllPets();
    }

    // 특정 플레이어가 소유한 펫의 리스트를 반환합니다.
    // Returns a list of pets owned by a specific player.
    public List<String> getPlayerPets(Player player) {
        return PetsUtils.getPlayerPets(player);
    }

    // 펫 ID로부터 펫 이름을 반환합니다.
    // Returns the pet name from a given pet ID.
    public String getPetNameFromId(String id) {
        return PetsUtils.getPetNameFromId(id);
    }

    // 펫 ID로부터 펫의 재질을 반환합니다.
    // Returns the material of the pet from a given pet ID.
    public String getPetMaterialFromId(String id) {
        return PetsUtils.getPetMaterialFromId(id);
    }

    // 펫 ID로부터 커스텀 모델 데이터를 반환합니다.
    // Returns the custom model data of the pet from a given pet ID.
    public int getPetCustomModelDataFromId(String id) {
        return PetsUtils.getPetCustomModelDataFromId(id);
    }

    // 펫 ID로부터 펫의 설명을 반환합니다.
    // Returns the lore of the pet from a given pet ID.
    public List<String> getPetLoreFromId(String id) {
        return PetsUtils.getPetLoreFromId(id);
    }

    // 펫 ID로부터 펫의 권한을 반환합니다.
    // Returns the permission of the pet from a given pet ID.
    public String getPetPermFromId(String id) {
        return PetsUtils.getPetPermFromId(id);
    }

    // 펫 ID로부터 카테고리를 반환합니다.
    // Returns the category of the pet from a given pet ID.
    public static String getCategoryOfPet(String petId) {
        return PetsUtils.getCategoryOfPet(petId);
    }

    // 카테고리 ID로부터 해당 카테고리의 펫 리스트를 반환합니다.
    // Returns a list of pets in a given category ID.
    public static List<String> getPetsInCategory(String categoryId) {
        return PetsUtils.getPetsInCategory(categoryId);
    }

    // 카테고리 ID로부터 카테고리 이름을 반환합니다.
    // Returns the category name by category ID.
    public static String getCategoryNameById(String categoryId) {
        return PetsUtils.getCategoryNameById(categoryId);
    }

    // 펫 ID로부터 카테고리 이름을 반환합니다.
    // Returns the category name by pet ID.
    public static Component getCategoryNameByPetId(String petId) {
        return PetsUtils.getCategoryNameByPetId(petId);
    }
}

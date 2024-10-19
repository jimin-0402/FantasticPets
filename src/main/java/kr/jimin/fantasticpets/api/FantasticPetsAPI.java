package kr.jimin.fantasticpets.api;

import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class FantasticPetsAPI {

    //
    public String getRandomPetId() {
        return PetsUtils.getRandomPetId();
    }

    public List<String> getAllPets() {
        return PetsUtils.getAllPets();
    }

    public List<String> getPlayerPets(Player player) {
        return PetsUtils.getPlayerPets(player);
    }
}

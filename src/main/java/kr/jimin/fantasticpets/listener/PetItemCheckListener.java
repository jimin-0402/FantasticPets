package kr.jimin.fantasticpets.listener;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.SoundsUtils;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PetItemCheckListener implements Listener {
    private final FantasticPetsPlugin plugin;

    public PetItemCheckListener(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        String clickTypeConfig = Config.SETTING_CLICK_TYPE.toString();
        if (clickTypeConfig.isEmpty()) clickTypeConfig = "RIGHT";
        if (ClickType.isValidClickType(event, clickTypeConfig, player)) return;

        if (item.getType() == Material.AIR) return;

        String petId = PetsFileManager.getPetItemsID(plugin, item);
        if (petId == null) return;

        String petPerm = PetsUtils.getPetPermFromId(petId);
        if (petPerm != null && player.hasPermission(petPerm)) {
            PetsUtils.isHasPlayerPet(player, petId, true);
            return;
        }

        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);
        if (petItem == null || petItem.getType() == Material.AIR) return;

        PetsUtils.addPetsPermPlayer(player, petId);
        player.getInventory().removeItem(petItem);

        PetsUtils.getCategoryMessage(player, petId);
        SoundsUtils.playSound(player, Config.SOUND_SUCCESS.toStringList());
        event.setCancelled(true);
    }
}

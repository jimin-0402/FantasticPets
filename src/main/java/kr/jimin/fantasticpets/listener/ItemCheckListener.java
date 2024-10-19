package kr.jimin.fantasticpets.listener;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.SoundsUtils;
import kr.jimin.fantasticpets.util.item.ItemHandler;
import kr.jimin.fantasticpets.util.pet.FantasticPetsUtils;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemCheckListener implements Listener {
    private final FantasticPetsPlugin plugin;

    public ItemCheckListener(FantasticPetsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack mainItem = player.getInventory().getItemInMainHand();

        String clickTypeConfig = Config.SETTING_CLICK_TYPE.toString();
        if (clickTypeConfig.isEmpty()) clickTypeConfig = "RIGHT";
        if (ClickType.isValidClickType(event, clickTypeConfig, player)) return;

        if (mainItem.getType() == Material.AIR) return;

        // Custom item check
        ItemStack customItem = Objects.requireNonNull(ItemHandler.create(
                Config.PET_ITEM_MATERIAL.toString(),
                Config.PET_ITEM_NAME.toString(),
                Config.PET_ITEM_LORE.toStringList(),
                1,
                Config.PET_ITEM_MODEL_DATA.toInt()
        )).getItem();

        if (customItem != null && mainItem.isSimilar(customItem)) {
            FantasticPetsUtils.randomGetPet(plugin, player, customItem);
            event.setCancelled(true);
            return;
        }

        // Pet item check
        String petId = PetsFileManager.getPetItemsID(plugin, mainItem);
        if (petId == null) {
            Message.PET_WITHOUT_PET.send(player);
            return;
        }
        String petPerm = PetsUtils.getPetPermFromId(petId);
        if (petPerm != null && player.hasPermission(petPerm)) {
            PetsUtils.isHasPlayerPet(player, petId, true);
            return;
        }

        ItemStack petItem = PetsFileManager.loadPetItems(plugin, petId);
        if (petItem != null && petItem.getType() != Material.AIR) {
            FantasticPetsUtils.addPetsPermPlayer(player, petId);
            player.getInventory().removeItem(petItem);
            FantasticPetsUtils.getCategoryMessage(player, petId);
            SoundsUtils.playSound(player, Config.SOUND_SUCCESS.toStringList());
            event.setCancelled(true);
        }
    }
}

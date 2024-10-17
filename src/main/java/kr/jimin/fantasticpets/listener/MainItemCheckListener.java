package kr.jimin.fantasticpets.listener;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Config;
import kr.jimin.fantasticpets.util.SoundsUtils;
import kr.jimin.fantasticpets.util.item.ItemHandler;
import kr.jimin.fantasticpets.util.pet.PetsUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MainItemCheckListener implements Listener {
    private final FantasticPetsPlugin plugin;

    public MainItemCheckListener(FantasticPetsPlugin plugin) {
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

        ItemStack customItem = Objects.requireNonNull(ItemHandler.create(
                Config.PET_ITEM_MATERIAL.toString(),
                Config.PET_ITEM_NAME.toString(),
                Config.PET_ITEM_LORE.toStringList(),
                1,
                Config.PET_ITEM_MODEL_DATA.toInt()
        )).getItem();

        if (customItem == null) return;

        if (mainItem.isSimilar(customItem)) {
            PetsUtils.randomGetPet(plugin, player, customItem);
            SoundsUtils.playSound(player, Config.SOUND_SUCCESS.toStringList());
            event.setCancelled(true);
        }
    }
}

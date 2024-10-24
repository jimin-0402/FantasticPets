package kr.jimin.fantastic.pets.util.item;

import kr.jimin.fantastic.pets.util.MessagesUtils;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.stream.Collectors;

public class ItemUtils {
    private final ItemStack item;
    private final String name;
    private final List<String> description;
    private final int amount;
    private final int modelData;

    public ItemUtils(ItemStack item, String name, List<String> description, int amount, int modelData) {
        this.item = item;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.modelData = modelData;
    }

    public void giveItem(Player player, int amount) {
        ItemStack iS = getItem();
        iS.setAmount(amount);
        player.getInventory().addItem(iS);
    }

    public ItemStack getUnmodifiedItem() {
        return item;
    }

    public ItemStack getItem() {
        ItemStack iS = getUnmodifiedItem();
        ItemMeta im = iS.getItemMeta();
        if (im == null) return iS;

        if (name != null) {
            im.displayName(MessagesUtils.processMessage(name).decoration(TextDecoration.ITALIC, false));
        }

        if (description != null) {
            List<net.kyori.adventure.text.Component> lore = description.stream()
                    .map(line -> MessagesUtils.processMessage(line).decoration(TextDecoration.ITALIC, false))
                    .collect(Collectors.toList());
            im.lore(lore);
        }

        if (modelData > 0) {
            im.setCustomModelData(modelData);
        }

        iS.setItemMeta(im);
        iS.setAmount(amount);
        return iS;
    }
}


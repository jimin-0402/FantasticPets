package kr.jimin.fantastic.pets.util.item.factories.impl;

import com.nexomc.nexo.api.NexoItems;
import kr.jimin.fantastic.pets.util.item.factories.ItemFactory;
import org.bukkit.inventory.ItemStack;

public class NexoFactory implements ItemFactory {
    @Override
    public ItemStack create(String id) {
        return NexoItems.itemFromId(id).build();
    }
}

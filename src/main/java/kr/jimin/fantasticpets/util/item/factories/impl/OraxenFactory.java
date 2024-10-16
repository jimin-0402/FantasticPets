package kr.jimin.fantasticpets.util.item.factories.impl;

import io.th0rgal.oraxen.api.OraxenItems;
import kr.jimin.fantasticpets.util.item.factories.ItemFactory;
import org.bukkit.inventory.ItemStack;

public class OraxenFactory implements ItemFactory {
    @Override
    public ItemStack create(String id) {
        return OraxenItems.getItemById(id).build();
    }
}


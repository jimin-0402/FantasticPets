package kr.jimin.fantastic.pets.util.item.factories.impl;

import io.th0rgal.oraxen.api.OraxenItems;
import kr.jimin.fantastic.pets.util.item.factories.ItemFactory;
import org.bukkit.inventory.ItemStack;

public class OraxenFactory implements ItemFactory {
    @Override
    public ItemStack create(String id) {
        return OraxenItems.getItemById(id).build();
    }
}


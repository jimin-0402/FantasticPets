package kr.jimin.fantasticpets.util.item.factories;

import org.bukkit.inventory.ItemStack;

public interface ItemFactory {
    ItemStack create(String id);
}
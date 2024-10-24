package kr.jimin.fantastic.pets.util.item.factories;

import org.bukkit.inventory.ItemStack;

public interface ItemFactory {
    ItemStack create(String id);
}
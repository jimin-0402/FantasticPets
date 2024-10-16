package kr.jimin.fantasticpets.util.item.factories.impl;

import dev.lone.itemsadder.api.CustomStack;
import kr.jimin.fantasticpets.util.item.factories.ItemFactory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class IAFactory implements ItemFactory {
    @Override
    public ItemStack create(String id) {
        return Objects.requireNonNull(CustomStack.getInstance(id)).getItemStack();
    }
}

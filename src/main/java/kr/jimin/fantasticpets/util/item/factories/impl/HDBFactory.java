package kr.jimin.fantasticpets.util.item.factories.impl;

import kr.jimin.fantasticpets.util.item.factories.ItemFactory;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.inventory.ItemStack;

public class HDBFactory implements ItemFactory {
    @Override
    public ItemStack create(String id) {
        return new HeadDatabaseAPI().getItemHead(id);
    }
}

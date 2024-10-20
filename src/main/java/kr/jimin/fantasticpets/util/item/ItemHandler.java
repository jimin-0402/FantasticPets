package kr.jimin.fantasticpets.util.item;

import kr.jimin.fantasticpets.util.item.factories.ItemFactory;
import kr.jimin.fantasticpets.util.item.factories.impl.HDBFactory;
import kr.jimin.fantasticpets.util.item.factories.impl.IAFactory;
import kr.jimin.fantasticpets.util.item.factories.impl.OraxenFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemHandler {

    private static final Map<String, ItemFactory> factories = new HashMap<>();

    static {
        factories.put("ORAXEN", new OraxenFactory());
        factories.put("ITEMSADDER", new IAFactory());
        factories.put("HDB", new HDBFactory());
    }

    public static ItemUtils create(
            String namespace,
            String name,
            List<String> description,
            int amount,
            int modeldata
    ) {
        ItemStack itemStack;
        if (namespace.contains(":")) {
            String id = namespace.split(":")[0].toUpperCase();
            ItemFactory factory = factories.get(id);
            if (factory == null) {
                return null;
            }
            itemStack = factory.create(namespace.substring(id.length() + 1));
        } else {
            itemStack = new ItemStack(Material.valueOf(namespace.toUpperCase()));
        }

        if (itemStack == null) {
            return null;
        }

        return create(itemStack, name, description, amount, modeldata);
    }

    public static ItemUtils create(
            ItemStack item,
            String name,
            List<String> description,
            int amount,
            int modeldata
    ) {
        return new ItemUtils(
                item,
                name,
                description,
                amount,
                modeldata
        );
    }
}

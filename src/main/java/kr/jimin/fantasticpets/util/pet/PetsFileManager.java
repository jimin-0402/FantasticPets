package kr.jimin.fantasticpets.util.pet;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Message;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.item.ItemUtils;
import kr.jimin.fantasticpets.util.item.ItemHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class PetsFileManager {
    private static final Map<String, ItemStack> items = new HashMap<>();

    public static ItemStack loadPetItems(FantasticPetsPlugin plugin, String petItemId) {
        return items.containsKey(petItemId) ? items.get(petItemId) : loadPIFromFile(plugin, petItemId);
    }

    private static ItemStack loadPIFromFile(FantasticPetsPlugin plugin, String petItemsID) {
        File file = new File(plugin.getDataFolder(), "Pets/" + petItemsID + ".yml");

        if (!file.exists()) {
            return null;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean isEnable = config.getBoolean("enable", false);
        if (!isEnable) {
            return null;
        }

        String material = config.getString("item.material", "STONE");
        int amount = config.getInt("item.amount", 1);
        String displayName = config.getString("item.display_name", petItemsID);
        List<String> lore = config.getStringList("item.lore");
        int customModelData = config.getInt("item.custom_model_data", 0);

        ItemUtils itemUtils = ItemHandler.create(material, displayName, lore, amount, customModelData);
        if (itemUtils == null) {
            return null;
        }

        return itemUtils.getItem();
    }

    public static void reloadPetItems(FantasticPetsPlugin plugin) {
        items.clear();
        File itemsFolder = new File(plugin.getDataFolder(), "Pets");

        File[] files = itemsFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files != null && files.length > 0) {
            for (File file : files) {
                String petItemId = file.getName().replace(".yml", "");
                ItemStack item = loadPIFromFile(plugin, petItemId);
                if (item != null) {
                    items.put(petItemId, item);
                }
            }
        } else {
            System.out.println("No pet files found in the Pets folder.");
        }
    }

    public static List<String> getPIList(FantasticPetsPlugin plugin) {
        File itemsFolder = new File(plugin.getDataFolder(), "Pets");
        File[] files = itemsFolder.listFiles();

        return (files != null) ?
                java.util.Arrays.stream(files)
                        .filter(file -> file.getName().endsWith(".yml"))
                        .filter(file -> {
                            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                            return config.getBoolean("enable", false);
                        })
                        .map(file -> file.getName().replace(".yml", ""))
                        .collect(Collectors.toList()) :
                List.of();
    }

    public static String getPetItemsID(FantasticPetsPlugin plugin, ItemStack item) {
        File itemsFolder = new File(plugin.getDataFolder(), "Pets");
        File[] files = itemsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".yml") && matchesFile(item, file)) {
                    return file.getName().replace(".yml", "");
                }
            }
        }
        return null;
    }

    private static boolean matchesFile(ItemStack item, File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        boolean isEnabled = config.getBoolean("enable", false);
        if (!isEnabled) return false;

        String material = config.getString("item.material", "STONE");
        String displayName = config.getString("item.display_name");
        List<String> lore = config.getStringList("item.lore");
        int customModelData = config.getInt("item.custom_model_data", 0);

        ItemUtils itemUtils = ItemHandler.create(material, displayName, lore, 1, customModelData);
        assert itemUtils != null;
        ItemStack createdItem = itemUtils.getItem();

        return item.isSimilar(createdItem);
    }

    public static String getRandomItemsID(FantasticPetsPlugin plugin) {
        if (items.isEmpty()) {
            reloadPetItems(plugin);
        }
        List<String> petIds = items.keySet().stream().toList();
        return petIds.isEmpty() ? null : petIds.get(new Random().nextInt(petIds.size()));
    }

}

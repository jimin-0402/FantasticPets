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
import java.util.stream.Collectors;

public class PetsFileManager {
    private static final Map<String, ItemStack> items = new HashMap<>();

//    public void onLoad() {
//        String[] petId = PetsUtils.getAllPets().toArray(new String[0]);
//        YamlUtils.create(i)
//    }

    public static ItemStack loadPetItems(FantasticPetsPlugin plugin, String ownItemsID) {
        return items.containsKey(ownItemsID) ? items.get(ownItemsID) : loadPIFromFile(plugin, ownItemsID);
    }

    private static ItemStack loadPIFromFile(FantasticPetsPlugin plugin, String petItemsID) {
        File file = new File(plugin.getDataFolder(), "Pets/" + petItemsID + ".yml");

        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        boolean isEnable = config.getBoolean("enable", false);
        if (!isEnable) return null;

        String material = config.getString("item.material", "STONE");
        int amount = config.getInt("item.amount", 1);
        String displayName = config.getString("item.display_name", petItemsID);
        List<String> lore = config.getStringList("item.lore");
        int customModelData = config.getInt("item.custom_model_data", 0);

        ItemUtils itemUtils = ItemHandler.create(material, displayName, lore, amount, customModelData);
        return itemUtils != null ? itemUtils.getItem() : null;
    }

    public static void reloadPetItems(FantasticPetsPlugin plugin) {
        items.clear();
        File itemsFolder = new File(plugin.getDataFolder(), "Pets");
        File[] files = itemsFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".yml")) {
                    String ownItemsID = file.getName().replace(".yml", "");
                    loadPIFromFile(plugin, ownItemsID);
                }
            }
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
        return items.keySet().stream().findAny().orElse(null);
    }

    public static void giveItem(FantasticPetsPlugin plugin, CommandSender sender, Player player, String petId, int amount) {
        ItemStack iS = loadPetItems(plugin, petId);

        if (iS == null) {
            Message.PET_NOT_FOUND.send(sender, MessagesUtils.tagResolver("pet-name", petId));
            return;
        }

        iS.setAmount(amount);
        player.getInventory().addItem(iS);
        Message.COMMAND_GIVE_PLAYER.send(sender, MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", player.displayName()));
        Message.COMMAND_GIVE_TARGET.send(player, MessagesUtils.tagResolver("pet-name", PetsUtils.getPetNameFromId(petId)), MessagesUtils.tagResolver("player", sender.name()));
    }
}

package kr.jimin.fantastic.pets;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import kr.jimin.fantastic.pets.command.CommandsManager;
import kr.jimin.fantastic.pets.config.ConfigsManager;
import kr.jimin.fantastic.pets.listener.ItemCheckListener;
import kr.jimin.fantastic.pets.util.logs.LogsManager;
import kr.jimin.fantastic.pets.util.pet.PetsFileManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public class FantasticPetsPlugin extends JavaPlugin {

    private static FantasticPetsPlugin fantasticPets;
    private ConfigsManager configsManager;
    private BukkitAudiences audience;

    public FantasticPetsPlugin() {
        fantasticPets = this;
    }

    public static FantasticPetsPlugin get() {
        return fantasticPets;
    }

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).silentLogs(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        audience = BukkitAudiences.create(this);

        reloadConfigs();
        configsManager.petCreate();

        new LogsManager(this).createLogsDirectory();

        new CommandsManager(this).loadCommands();

        getServer().getPluginManager().registerEvents(new ItemCheckListener(this), this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public void reloadConfigs() {
        configsManager = new ConfigsManager(this);
        configsManager.validatesConfig();
        PetsFileManager.reloadPetItems(this);
    }

    public BukkitAudiences getAudience() {
        return audience;
    }

    public ConfigsManager getConfigsManager() {
        return configsManager;
    }
}

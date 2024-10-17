package kr.jimin.fantasticpets;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import kr.jimin.fantasticpets.command.CommandsManager;
import kr.jimin.fantasticpets.command.TestCommand;
import kr.jimin.fantasticpets.config.ConfigsManager;
import kr.jimin.fantasticpets.listener.MainItemCheckListener;
import kr.jimin.fantasticpets.listener.PetItemCheckListener;
import kr.jimin.fantasticpets.util.pet.PetsFileManager;
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

        new CommandsManager(this).loadCommands();
        new TestCommand(this).loadCommands();

        getServer().getPluginManager().registerEvents(new MainItemCheckListener(this), this);
        getServer().getPluginManager().registerEvents(new PetItemCheckListener(this), this);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }

    public BukkitAudiences getAudience() {
        return audience;
    }

    public void reloadConfigs() {
        configsManager = new ConfigsManager(this);
        configsManager.validatesConfig();
        PetsFileManager.reloadPetItems(this);
    }

    public ConfigsManager getConfigsManager() {
        return configsManager;
    }
}

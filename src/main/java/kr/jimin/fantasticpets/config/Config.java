package kr.jimin.fantasticpets.config;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.util.MessagesUtils;
import kr.jimin.fantasticpets.util.logs.Logs;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public enum Config {

    DEBUG("debug"),

    // plugin
    SETTING_LANGUAGE("Setting.language"),
    SETTING_CLICK_TYPE("Setting.cluck-type"),

    // pet
    PET_DUPLICATION("Pet.duplication"),
    PET_ITEM_MATERIAL("Pet.item.material"),
    PET_ITEM_NAME("Pet.item.display-name"),
    PET_ITEM_LORE("Pet.item.lore"),
    PET_ITEM_MODEL("Pet.item.model-data"),

    // sound
    SOUND_SUCCESS("sound.success"),
    SOUND_FAIL("sound.fail");

    private final String path;

    Config(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return FantasticPetsPlugin.get().getConfigsManager().getConfigs().get(path);
    }
    public void setValue(Object value) { setValue(value, true); }
    public void setValue(Object value, boolean save) {
        YamlConfiguration settingFile = FantasticPetsPlugin.get().getConfigsManager().getConfigs();
        settingFile.set(path, value);
        try {
            if (save) settingFile.save(FantasticPetsPlugin.get().getDataFolder().toPath().resolve("settings.yml").toFile());
        } catch (Exception e) {
            Logs.logError("Failed to apply changes to settings.yml");
        }
    }

    @Override
    public String toString() {
        return (String) getValue();
    }

    public Component toComponent() {
        return MessagesUtils.MINI_MESSAGE_WITH_TAGS.deserialize(getValue().toString());
    }

    public Boolean toBool() {
        return (Boolean) getValue();
    }

    public List<String> toStringList() {
        return FantasticPetsPlugin.get().getConfigsManager().getConfigs().getStringList(path);
    }

    public ConfigurationSection toConfigSection() {
        return FantasticPetsPlugin.get().getConfigsManager().getConfigs().getConfigurationSection(path);
    }

}

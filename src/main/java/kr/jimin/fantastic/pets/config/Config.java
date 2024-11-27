package kr.jimin.fantastic.pets.config;

import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import net.kyori.adventure.text.Component;

import java.util.List;

public enum Config {

    DEBUG("debug"),

    // setting
    SETTING_LANGUAGE("Setting.language"),
    SETTING_CLICK_TYPE("Setting.cluck-type"),
    SETTING_LOG("Setting.log"),
    SETTING_MESSAGE_TYPE("Setting.message-type"),

    // pet
    PET_ALLOW_DUPLICATION("Pet.allow-duplicates"),
    PET_USE_CATEGORY("Pet.use-category"),
    PET_USE_CHANCE("Pet.use-chance"),
    PET_ITEM_MATERIAL("Pet.item.material"),
    PET_ITEM_NAME("Pet.item.display-name"),
    PET_ITEM_LORE("Pet.item.lore"),
    PET_ITEM_MODEL_DATA("Pet.item.model-data"),

    // Title
    Title_FADE_IN("Title.fadeIn"),
    Title_STAY("Title.stay"),
    Title_FADE_OUT("Title.fadeOut"),

    // sound
    SOUND_SUCCESS("Sound.success"),
    SOUND_FAIL("Sound.fail");

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

    @Override
    public String toString() {
        return (String) getValue();
    }

    public Component toComponent() {
        return MessagesUtils.processMessage(getValue().toString());
    }

    public Boolean toBool() {
        return (Boolean) getValue();
    }

    public List<String> toStringList() {
        return FantasticPetsPlugin.get().getConfigsManager().getConfigs().getStringList(path);
    }

    public int toInt() { return (int) getValue(); }

}

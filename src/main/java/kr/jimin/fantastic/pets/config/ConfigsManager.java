package kr.jimin.fantastic.pets.config;

import kr.jimin.fantastic.pets.util.YamlUtils;
import kr.jimin.fantastic.pets.util.logs.Logs;
import kr.jimin.fantastic.pets.util.pet.PetsUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigsManager {

    private final JavaPlugin plugin;
    private final YamlConfiguration defaultConfig;
    private final YamlConfiguration defaultLanguage;
    private YamlConfiguration config;
    private YamlConfiguration language;
    private File petsFolder;

    public ConfigsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        defaultConfig = extractDefault("config.yml");
        defaultLanguage = extractDefault("languages/korean.yml");
    }

    public YamlConfiguration getConfigs() {
        return config != null ? config : defaultConfig;
    }

    public YamlConfiguration getLanguage() {
        return language != null ? language : defaultLanguage;
    }

    private YamlConfiguration extractDefault(String source) {
        InputStreamReader inputStreamReader = new InputStreamReader(plugin.getResource(source));
        try {
            return YamlUtils.loadConfiguration(inputStreamReader);
        } finally {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                Logs.logWarning("Failed to extract default file: " + source);
                if (Config.DEBUG.toBool()) e.printStackTrace();
            }
        }
    }

    public void validatesConfig() {
        config = validate("config.yml", defaultConfig);
        File languagesFolder = new File(plugin.getDataFolder(), "languages");
        languagesFolder.mkdir();
        String languageFile = "languages/" + config.getString(Config.SETTING_LANGUAGE.getPath()) + ".yml";
        language = validate(languageFile, defaultLanguage);

        petsFolder = new File(plugin.getDataFolder(), "Pets");
        if (!petsFolder.exists()) { petsFolder.mkdir(); }
    }

    private YamlConfiguration validate(String configName, YamlConfiguration defaultConfiguration) {
        File configurationFile = extractConfiguration(configName);
        return YamlUtils.loadConfiguration(configurationFile);
    }

    public File extractConfiguration(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists())
            this.plugin.saveResource(fileName, false);
        return file;
    }

    public void petCreate() {
        List<String> petIds = PetsUtils.getAllPets();

        for (String petId : petIds) {
            File file = new File(petsFolder, petId + ".yml");

            if (file.exists()) continue;

            try {
                InputStream inputStream = getClass().getResourceAsStream("/example.yml");
                if (inputStream != null) {
                    YamlConfiguration exampleConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                    YamlConfiguration newConfig = new YamlConfiguration();

                    for (String key : exampleConfig.getKeys(true)) {
                        newConfig.set(key, exampleConfig.get(key));
                    }

                    newConfig.set("item.material", PetsUtils.getPetMaterialFromId(petId));
                    newConfig.set("item.display_name", PetsUtils.getPetNameFromId(petId));
                    newConfig.set("item.lore", PetsUtils.getPetLoreFromId(petId));
                    newConfig.set("item.custom_model_data", PetsUtils.getPetCustomModelDataFromId(petId));

                    newConfig.save(file);
                } else {
                    Logs.logWarning("The example.yml file does not exist.");
                }
            } catch (IOException e) {
                Logs.logWarning("Error copying example.yml content: " + e.getMessage());
            }
        }
    }


}

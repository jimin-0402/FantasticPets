package kr.jimin.fantastic.pets.config;

import kr.jimin.fantastic.pets.util.YamlUtils;
import kr.jimin.fantastic.pets.util.logs.Logs;
import kr.jimin.fantastic.pets.api.FantasticPetsAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ConfigsManager extends FantasticPetsAPI {

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
        InputStream inputStream = plugin.getResource(source);
        if (inputStream == null) {
            Logs.logWarning("Default file not found: " + source);
            return null;
        }
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            return YamlUtils.loadConfiguration(inputStreamReader);
        } catch (IOException e) {
            Logs.logWarning("Failed to load default file: " + source);
            if (Config.DEBUG.toBool()) e.printStackTrace();
            return null;
        }
    }

    public void validatesConfig() {
        config = validate("config.yml", defaultConfig);
        File languagesFolder = new File(plugin.getDataFolder(), "languages");
        if (!languagesFolder.exists()) {
            languagesFolder.mkdir();
        }
        String languageFile = "languages/" + config.getString(Config.SETTING_LANGUAGE.getPath()) + ".yml";
        language = validate(languageFile, defaultLanguage);

        petsFolder = new File(plugin.getDataFolder(), "Pets");
        if (!petsFolder.exists()) {
            petsFolder.mkdir();
        }
    }

    private YamlConfiguration validate(String configName, YamlConfiguration defaultConfiguration) {
        File configurationFile = extractConfiguration(configName);
        YamlConfiguration loadedConfig = YamlUtils.loadConfiguration(configurationFile);
        if (loadedConfig == null) {
            Logs.logWarning("Failed to load configuration for: " + configName);
            return defaultConfiguration;
        }
        return loadedConfig;
    }

    public File extractConfiguration(String fileName) {
        File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false);
        }
        return file;
    }

    public void petCreate() {
        List<String> petIds = getAllPets();

        for (String petId : petIds) {
            File file = new File(petsFolder, petId + ".yml");

            if (file.exists()) continue;

            try (InputStream inputStream = getClass().getResourceAsStream("/example.yml")) {
                if (inputStream != null) {
                    YamlConfiguration exampleConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
                    YamlConfiguration newConfig = new YamlConfiguration();

                    for (String key : exampleConfig.getKeys(true)) {
                        newConfig.set(key, exampleConfig.get(key));
                    }

                    newConfig.set("item.material", getPetMaterialFromId(petId));
                    newConfig.set("item.display_name", getPetNameFromId(petId));
                    newConfig.set("item.lore", getPetLoreFromId(petId));
                    newConfig.set("item.custom_model_data", getPetCustomModelDataFromId(petId));

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

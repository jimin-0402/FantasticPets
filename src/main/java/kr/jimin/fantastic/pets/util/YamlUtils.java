package kr.jimin.fantastic.pets.util;

import kr.jimin.fantastic.pets.config.Config;
import kr.jimin.fantastic.pets.util.logs.Logs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class YamlUtils extends YamlConfiguration {

    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull File file) throws RuntimeException {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (InvalidConfigurationException e) {
            Logs.logWarning("Error loading YAML configuration file: " + file.getName());
            Logs.logWarning("Ensure that your config is formatted correctly:");
            Logs.logWarning(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public void load(@NotNull File file) {
        try {
            super.load(file);
        } catch (Exception e) {
            Logs.logWarning("Error loading YAML configuration file: " + file.getName());
            if (Config.DEBUG.toBool()) Logs.logWarning(e.getMessage());
        }
    }
}

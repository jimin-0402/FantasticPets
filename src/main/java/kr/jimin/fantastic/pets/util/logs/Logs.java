package kr.jimin.fantastic.pets.util.logs;

import kr.jimin.fantastic.pets.FantasticPetsPlugin;

public class Logs {
    private FantasticPetsPlugin plugin;

    private Logs() {}

    public static void logInfo(String message) {;
        FantasticPetsPlugin.get().getLogger().info(message);
    }

    public static void logWarning(String message) {
        FantasticPetsPlugin.get().getLogger().warning(message);
    }

    public static void logSevere(String message) {
        FantasticPetsPlugin.get().getLogger().severe(message);
    }

}
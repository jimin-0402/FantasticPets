package kr.jimin.fantastic.pets.util.logs;

import kr.jimin.fantastic.pets.FantasticPetsPlugin;
import kr.jimin.fantastic.pets.util.MessagesUtils;
import net.kyori.adventure.text.Component;

public class Logs {

    private Logs() {}

    public static void logInfo(String message) {
        if (!message.isEmpty()) logInfo(message, false);
    }

    public static void logInfo(String message, boolean newline) {
        Component info = MessagesUtils.MINI_MESSAGE.deserialize("<prefix><#529ced>" + message + "</#529ced>");
        FantasticPetsPlugin.get().getAudience().console().sendMessage(newline ? info.append(Component.newline()) : info);
    }

    public static void logSuccess(String message) {
        logSuccess(message, false);
    }

    public static void logSuccess(String message, boolean newline) {
        Component success = MessagesUtils.MINI_MESSAGE.deserialize("<prefix><#55ffa4>" + message + "</#55ffa4>");
        FantasticPetsPlugin.get().getAudience().console().sendMessage(newline ? success.append(Component.newline()) : success);
    }

    public static void logError(String message) {
        logError(message, false);
    }

    public static void logError(String message, boolean newline) {
        Component error = MessagesUtils.MINI_MESSAGE.deserialize("<prefix><#e73f34>" + message + "</#e73f34>");
        FantasticPetsPlugin.get().getAudience().console().sendMessage(newline ? error.append(Component.newline()) : error);
    }

    public static void logWarning(String message) {
        logWarning(message, false);
    }

    public static void logWarning(String message, boolean newline) {
        Component warning = MessagesUtils.MINI_MESSAGE.deserialize("<prefix><#f9f178>" + message + "</#f9f178>");
        FantasticPetsPlugin.get().getAudience().console().sendMessage(newline ? warning.append(Component.newline()) : warning);
    }

    public static void newline() {
        FantasticPetsPlugin.get().getAudience().console().sendMessage(Component.empty());
    }

}
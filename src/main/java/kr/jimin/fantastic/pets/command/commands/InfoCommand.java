package kr.jimin.fantastic.pets.command.commands;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class InfoCommand {

    public CommandAPICommand getInfoCommand() {
        return new CommandAPICommand("info")
                .withPermission("fantasticpets.command.info")
                .executes((sender, args) -> {
                    printPluginVersion(sender, "FantasticPets");
                    printPluginVersion(sender, "MCPets");
                    printPluginVersion(sender, "ModelEngine");
                    printPluginVersion(sender, "MythicMobs");
                    printPluginVersion(sender, "LuckPerms");

                    printServerVersion(sender);
                    printJavaVersion(sender);
                });
    }

    private void printPluginVersion(CommandSender sender, String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            String version = plugin.getDescription().getVersion();
            sender.sendMessage(pluginName + " - " + version);
        }
    }

    private void printServerVersion(CommandSender sender) {
        String serverVersion = Bukkit.getVersion();
        sender.sendMessage("Server version: " + serverVersion);
    }

    private void printJavaVersion(CommandSender sender) {
        String javaVersion = System.getProperty("java.version");
        sender.sendMessage("Java version: " + javaVersion);
    }
}

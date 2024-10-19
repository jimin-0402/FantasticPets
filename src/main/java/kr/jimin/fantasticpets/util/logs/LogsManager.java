package kr.jimin.fantasticpets.util.logs;

import kr.jimin.fantasticpets.FantasticPetsPlugin;
import kr.jimin.fantasticpets.config.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class LogsManager {

    private final FantasticPetsPlugin plugin;
    private final File logsDirectory;

    private static final String LOGS_DIRECTORY_NAME = "Logs";
    private static final String SERVER_LOG_DIR_FORMAT = "server/%d-%02d-%02d";
    private static final String COMMAND_LOG_FILENAME = "command.txt";
    private static final String USER_LOG_FILENAME = "user.txt";

    public LogsManager(FantasticPetsPlugin plugin) {
        this.plugin = plugin;

        this.logsDirectory = new File(plugin.getDataFolder(), LOGS_DIRECTORY_NAME);
        createLogsDirectory();
    }

    public void createLogsDirectory() {
        if (!logsDirectory.exists()) {
            logsDirectory.mkdirs();
        }
    }

    private File getServerLogFolder() {
        LocalDateTime now = LocalDateTime.now();
        return new File(logsDirectory, String.format(SERVER_LOG_DIR_FORMAT, now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
    }

    private void createLogFile(File logFile) {
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commandLog(String type, String adminName, String playerName, String petId) {
        Player player = Bukkit.getPlayer(playerName);
        UUID playerId = (player != null) ? player.getUniqueId() : null;

        String commandType = switch (type) {
            case "give" -> "Give";
            case "take" -> "Take";
            case "item" -> "Item";
            default -> "";
        };

        String timestamp = getCurrentTimestamp();

        String log = Message.LOGS_COMMAND.toString()
                .replace("<date>", timestamp)
                .replace("<type>", commandType)
                .replace("<player-name>", playerName)
                .replace("<admin-name>", adminName)
                .replace("<pet-id>", petId);

        if (playerId != null) {
            log = log.replace("<player-id>", playerId.toString());
        }

        writeLogToFile(new File(getServerLogFolder(), COMMAND_LOG_FILENAME), log);
    }

    public void logUser(String playerName, String petName) {
        String timestamp = getCurrentTimestamp();
        String logEntry = Message.LOGS_USER.toString()
                .replace("<player>", playerName)
                .replace("<pet>", petName)
                .replace("<date>", timestamp);

        writeLogToFile(new File(getServerLogFolder(), USER_LOG_FILENAME), logEntry);
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(Message.LOGS_DATE.toString()));
    }

    private void writeLogToFile(File logFile, String logEntry) {
        File logFolder = logFile.getParentFile();
        if (!logFolder.exists()) {
            logFolder.mkdirs();
        }

        createLogFile(logFile);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

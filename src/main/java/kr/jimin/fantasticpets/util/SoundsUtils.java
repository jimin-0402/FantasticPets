package kr.jimin.fantasticpets.util;

import kr.jimin.fantasticpets.util.logs.Logs;
import org.bukkit.entity.Player;

import java.util.List;

public class SoundsUtils {

    public static void playSound(Player player, List<String> sound) {
        if (sound.isEmpty()) return;

        String[] soundData = String.join(",", sound).split(",");
        if (soundData.length < 3) return;

        String soundName = soundData[0].trim();
        String volumeString = soundData[1].trim().replaceAll("/]$", ""); // remove suffix "]"
        String pitchString = soundData[2].trim().replaceAll("/]$", ""); // remove suffix "]"

        Float volume = parseFloatOrNull(volumeString);
        Float pitch = parseFloatOrNull(pitchString);

        if (volume == null || pitch == null) {
            Logs.logError("Invalid volume or pitch: volume='" + volumeString + "', pitch='" + pitchString + "'");
            return;
        }

        player.playSound(player, soundName, volume, pitch);
    }

    private static Float parseFloatOrNull(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

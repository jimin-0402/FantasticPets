package kr.jimin.fantasticpets.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public enum ClickType {
    RIGHT,
    LEFT,
    RIGHT_SHIFT,
    LEFT_SHIFT;

    public static ClickType fromString(String type) {
        switch (type) {
            case "RIGHT":
                return RIGHT;
            case "LEFT":
                return LEFT;
            case "RIGHT:SHIFT":
                return RIGHT_SHIFT;
            case "LEFT:SHIFT":
                return LEFT_SHIFT;
            default:
                return null;
        }
    }

    private static boolean isClickTypeValid(Action action, boolean isSneaking, ClickType clickType) {
        switch (clickType) {
            case RIGHT:
                return action == Action.RIGHT_CLICK_AIR && !isSneaking;
            case LEFT:
                return action == Action.LEFT_CLICK_AIR && !isSneaking;
            case RIGHT_SHIFT:
                return action == Action.RIGHT_CLICK_AIR && isSneaking;
            case LEFT_SHIFT:
                return action == Action.LEFT_CLICK_AIR && isSneaking;
            default:
                return false;
        }
    }

    public static boolean isValidClickType(PlayerInteractEvent event, String clickTypeConfig, Player player) {
        ClickType clickType = fromString(clickTypeConfig);
        if (clickType == null) return false;

        // if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) return false;

        return isClickTypeValid(event.getAction(), player.isSneaking(), clickType);
    }
}
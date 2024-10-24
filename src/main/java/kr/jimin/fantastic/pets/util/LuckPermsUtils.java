package kr.jimin.fantastic.pets.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;

public class LuckPermsUtils {
    public static User getLuckPermsUser(Player player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        return luckPerms.getPlayerAdapter(Player.class).getUser(player);
    }
}

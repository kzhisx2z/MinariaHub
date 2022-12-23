package fr.kx2zh.minaria.hub.common.managers;

import org.bukkit.entity.Player;

public interface EntryPoints {

    void onDisable();
    void onLogin(Player player);
    void onLogout(Player player);

}

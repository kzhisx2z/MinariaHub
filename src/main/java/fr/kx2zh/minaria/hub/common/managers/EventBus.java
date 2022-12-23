package fr.kx2zh.minaria.hub.common.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventBus implements EntryPoints {

    private final List<AbstractManager> managers = new ArrayList<>();

    public void registerManager(AbstractManager abstractManager) {
        managers.add(abstractManager);
    }

    @Override
    public void onDisable() {
        managers.forEach(EntryPoints::onDisable);
    }

    @Override
    public void onLogin(Player player) {
        managers.forEach(manager -> manager.onLogin(player));
    }

    @Override
    public void onLogout(Player player) {
        managers.forEach(manager -> manager.onLogin(player));
    }
}

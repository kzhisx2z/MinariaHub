package fr.kx2zh.minaria.hub.commands;

import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.commands.moderating.CommandAnnounce;
import fr.kx2zh.minaria.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class CommandManager extends AbstractManager {
    public CommandManager(Hub hub) {
        super(hub);
        registerCommands();
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLogin(Player player) {
    }

    @Override
    public void onLogout(Player player) {
    }

    private void registerCommands() {
        registerCommand("announce", "hub.announce", CommandAnnounce.class);
    }

    private void registerCommand(String tag, String permission, Class<? extends AbstractCommand> command) {
        try {
            final AbstractCommand abstractCommand = command.getDeclaredConstructor(Hub.class).newInstance(hub);
            abstractCommand.setPermission(permission);

            hub.getCommand(tag).setExecutor(abstractCommand);
            log(Level.INFO, "Registered command '" + tag + "'");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

package fr.kx2zh.minaria.hub.commands;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public abstract class AbstractCommand implements CommandExecutor {

    protected final Hub hub;
    private String permission;

    public AbstractCommand(Hub hub) {
        this.hub = hub;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Vous devez être un joueur en jeu pour pouvoir utiliser cette commande.");
            return true;
        }

        if (permission != null) {
            if (!hasPermission((Player) commandSender)) {
                commandSender.sendMessage(ChatColor.RED + "Erreur: Vous n'avez pas la permission d'utiliser la commande " + ChatColor.WHITE + command.getName() + ChatColor.RED + ".");
                return true;
            }
        }

        return doAction((Player) commandSender, command, s, strings);
    }

    public abstract boolean doAction(Player player, Command command, String label, String[] args);

    public void setPermission(String permission) {
        this.permission = permission;
    }

    protected boolean hasPermission(Player player) {
        return MinariaAPI.get().getPermissionManager().hasPermission(player, permission);
    }
}

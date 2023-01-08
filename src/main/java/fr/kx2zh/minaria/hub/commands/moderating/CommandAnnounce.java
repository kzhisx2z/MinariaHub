package fr.kx2zh.minaria.hub.commands.moderating;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandAnnounce extends AbstractCommand {

    public CommandAnnounce(Hub hub) {
        super(hub);
    }

    @Override
    public boolean doAction(Player player, Command command, String label, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Erreur: L'argument " + ChatColor.WHITE + "message" + ChatColor.RED + " est manquant.");
            return true;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        final String groupTag = MinariaAPI.get().getPermissionManager().getPlayer(player.getUniqueId()).getTag();
        Arrays.asList(args).forEach(s -> stringBuilder.append(s).append(" "));

        hub.getServer().getOnlinePlayers().forEach(player1 -> {
            player1.sendMessage(ChatColor.GOLD + "[Annonce] " + groupTag + " " + player.getName() + ChatColor.WHITE + ": " + ChatColor.translateAlternateColorCodes('&', stringBuilder.toString()));
        });

        return false;
    }
}

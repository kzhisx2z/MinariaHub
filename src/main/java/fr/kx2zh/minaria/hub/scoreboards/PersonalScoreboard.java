package fr.kx2zh.minaria.hub.scoreboards;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.api.player.AbstractPlayerData;
import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.utils.NumberUtils;
import fr.kx2zh.minaria.hub.utils.RankUtils;
import fr.kx2zh.minaria.tools.scoreboard.ObjectiveSign;
import fr.kx2zh.minaria.tools.scoreboard.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class PersonalScoreboard {

    private final Hub hub;
    private final Player player;
    private final ObjectiveSign objectiveSign;

    private SimpleDateFormat dateFormat;
    private String rank;
    private long coins;
    private long stars;

    public PersonalScoreboard(Hub hub, Player player) {
        this.hub = hub;
        this.player = player;

        objectiveSign = new ObjectiveSign(MinariaAPI.get().getServerName().toLowerCase(), "Minaria");

        reloadData();
        objectiveSign.addReceiver(player);

        for (ScoreboardTeam team : hub.getTeams()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(team.createTeam());
        }

        for (Player player1 : hub.getServer().getOnlinePlayers()) {
            for (Player player2 : hub.getServer().getOnlinePlayers()) {
                final Optional<ScoreboardTeam> sbTeam = getSbTeam(MinariaAPI.get().getPermissionManager().getPlayer(player1.getUniqueId()).getTag());

                sbTeam.ifPresent(scoreboardTeam -> {
                    ((CraftPlayer) player1).getHandle().playerConnection.sendPacket(scoreboardTeam.addOrRemovePlayer(3, player2.getName()));
                });
            }
        }
    }

    public void onLogout() {
        objectiveSign.removeReceiver(hub.getServer().getOfflinePlayer(player.getUniqueId()));
    }

    public void reloadData() {
        final AbstractPlayerData playerData = MinariaAPI.get().getPlayerDataManager().getPlayerData(player.getUniqueId());

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        rank = RankUtils.getFormattedRank(player.getUniqueId());
        coins = playerData.getCoins();
        stars = playerData.getStars();
    }

    public void setLines(String ip) {
        objectiveSign.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + " Minaria ");
        objectiveSign.setLine(0, ChatColor.DARK_GRAY + dateFormat.format(new Date()) + " " + MinariaAPI.get().getServerName().toLowerCase());

        objectiveSign.setLine(1, ChatColor.BLUE + "");

        objectiveSign.setLine(2, ChatColor.GOLD + "» " + ChatColor.WHITE + player.getName());
        objectiveSign.setLine(3, ChatColor.GRAY + "   Grade: " + rank);
        objectiveSign.setLine(4, ChatColor.GRAY + "   Coins: " + ChatColor.AQUA + NumberUtils.format(coins));
        objectiveSign.setLine(5, ChatColor.GRAY + "   Stars: " + ChatColor.LIGHT_PURPLE + NumberUtils.format(stars));

        objectiveSign.setLine(6, ChatColor.GREEN + "");

        objectiveSign.setLine(7, ChatColor.YELLOW + ip);

        objectiveSign.updateLines();
    }

    private Optional<ScoreboardTeam> getSbTeam(String prefix) {
        return hub.getTeams().stream().filter(scoreboardTeam -> scoreboardTeam.getPrefix().equalsIgnoreCase(prefix)).findFirst();
    }
}


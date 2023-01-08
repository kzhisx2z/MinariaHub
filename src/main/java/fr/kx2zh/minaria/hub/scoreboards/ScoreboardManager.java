package fr.kx2zh.minaria.hub.scoreboards;

import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.common.managers.AbstractManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScoreboardManager extends AbstractManager {

    private final Map<UUID, PersonalScoreboard> scoreboards = new HashMap<>();

    private final ScheduledFuture<?> glowingTask;
    private final ScheduledFuture<?> reloadingTask;
    private int ipCharIndex = 0;
    private int cooldown = 0;

    public ScoreboardManager(Hub hub) {
        super(hub);

        glowingTask = hub.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            final String ip = colorIpAt();

            for (PersonalScoreboard scoreboard : scoreboards.values()) {
                hub.getExecutorMonoThread().execute(() -> scoreboard.setLines(ip));
            }
        }, 80, 80, TimeUnit.MILLISECONDS);

        reloadingTask = hub.getScheduledExecutorService().scheduleAtFixedRate(() -> {
            for (PersonalScoreboard scoreboard : scoreboards.values()) {
                hub.getScheduledExecutorService().execute(scoreboard::reloadData);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onDisable() {
        glowingTask.cancel(true);
        reloadingTask.cancel(true);

        scoreboards.values().forEach(PersonalScoreboard::onLogout);
    }

    @Override
    public void onLogin(Player player) {
        if(scoreboards.containsKey(player.getUniqueId())) return;

        scoreboards.put(player.getUniqueId(), new PersonalScoreboard(hub, player));
    }

    @Override
    public void onLogout(Player player) {
        if(scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).onLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }

    private String colorIpAt() {
        final String ip = "play.minaria.fr";

        if (cooldown > 0) {
            cooldown--;
            return ip;
        }

        final StringBuilder formattedIp = new StringBuilder();

        if (ipCharIndex > 0) {
            formattedIp.append(ip, 0, ipCharIndex - 1);
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(ipCharIndex - 1));
        } else {
            formattedIp.append(ip, 0, ipCharIndex);
        }

        formattedIp.append(ChatColor.RED).append(ip.charAt(ipCharIndex));

        if (ipCharIndex + 1 < ip.length()) {
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(ipCharIndex + 1));

            if (ipCharIndex + 2 < ip.length()) formattedIp.append(ChatColor.YELLOW).append(ip.substring(ipCharIndex + 2));

            ipCharIndex++;
        } else {
            ipCharIndex = 0;
            cooldown = 50;
        }

        return formattedIp.toString();
    }
}

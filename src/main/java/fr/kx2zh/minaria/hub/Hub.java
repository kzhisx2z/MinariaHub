package fr.kx2zh.minaria.hub;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.hub.commands.CommandManager;
import fr.kx2zh.minaria.hub.common.managers.EventBus;
import fr.kx2zh.minaria.hub.common.players.PlayerManager;
import fr.kx2zh.minaria.hub.events.DoubleJumpListener;
import fr.kx2zh.minaria.hub.events.GuiListener;
import fr.kx2zh.minaria.hub.events.PlayerListener;
import fr.kx2zh.minaria.hub.events.protection.EntityEditionListener;
import fr.kx2zh.minaria.hub.events.protection.PlayerProtectionListener;
import fr.kx2zh.minaria.hub.events.protection.WoldEditionListener;
import fr.kx2zh.minaria.hub.gui.GuiManager;
import fr.kx2zh.minaria.hub.scoreboards.ScoreboardManager;
import fr.kx2zh.minaria.tools.scoreboard.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Hub extends JavaPlugin {
    public final static String SERVER_NAME = ChatColor.GOLD + "[Minaria] " + ChatColor.WHITE;
    public final static ChatColor PRIMARY_COLOR = ChatColor.GOLD;
    public final static ChatColor SECONDARY_COLOR = ChatColor.YELLOW;

    private World world;

    private final List<ScoreboardTeam> teams = new ArrayList<>();

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;


    private EventBus eventBus;
    private PlayerManager playerManager;
    private GuiManager guiManager;
    private ScoreboardManager scoreboardManager;
    private CommandManager commandManager;


    @Override
    public void onEnable() {
        saveDefaultConfig();

        world = getServer().getWorlds().get(0);
        world.setGameRuleValue("randomTickSpeed", "0");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(6000L);

        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);

        eventBus = new EventBus();
        playerManager = new PlayerManager(this);
        scoreboardManager = new ScoreboardManager(this);
        guiManager = new GuiManager(this);
        commandManager = new CommandManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
        getServer().getPluginManager().registerEvents(new DoubleJumpListener(this), this);
        getServer().getPluginManager().registerEvents(new WoldEditionListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEditionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerProtectionListener(), this);
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            MinariaAPI.get().getPlayerDataManager().getAllGroups().forEach((s, integer) -> teams.add(new ScoreboardTeam(Integer.toString(integer), s)));
        }, 5 * 20);
    }

    public World getWorld() {
        return world;
    }

    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public List<ScoreboardTeam> getTeams() {
        return teams;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

}

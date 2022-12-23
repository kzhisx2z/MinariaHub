package fr.kx2zh.minaria.hub;

import fr.kx2zh.minaria.hub.common.managers.EventBus;
import fr.kx2zh.minaria.hub.common.players.PlayerManager;
import fr.kx2zh.minaria.hub.events.DoubleJumpListener;
import fr.kx2zh.minaria.hub.events.PlayerListener;
import fr.kx2zh.minaria.hub.events.protection.EntityEditionListener;
import fr.kx2zh.minaria.hub.events.protection.PlayerProtectionListener;
import fr.kx2zh.minaria.hub.events.protection.WoldEditionListener;
import fr.kx2zh.minaria.hub.gui.GuiManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Hub extends JavaPlugin {
    public final static String SERVER_NAME = ChatColor.GOLD + "[Minaria] " + ChatColor.WHITE;

    private World world;

    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;

    private EventBus eventBus;
    private PlayerManager playerManager;
    private GuiManager guiManager;


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
        guiManager = new GuiManager(this);

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new DoubleJumpListener(this), this);
        getServer().getPluginManager().registerEvents(new WoldEditionListener(), this);
        getServer().getPluginManager().registerEvents(new EntityEditionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerProtectionListener(), this);
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

    public EventBus getEventBus() {
        return eventBus;
    }
}

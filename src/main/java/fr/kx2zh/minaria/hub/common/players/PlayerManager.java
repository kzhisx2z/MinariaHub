package fr.kx2zh.minaria.hub.common.players;


import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.common.managers.AbstractManager;
import fr.kx2zh.minaria.tools.InventoryUtils;
import fr.kx2zh.minaria.tools.LocationUtils;
import fr.kx2zh.minaria.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlayerManager extends AbstractManager {

    public static final float WALK_SPEED = 0.20F;
    public static final float FLY_SPEED = 0.20F;

    private final Location spawn;
    private final StaticInventory staticInventory;

    public PlayerManager(Hub hub) {
        super(hub);

        staticInventory = new StaticInventory(hub);

        spawn = LocationUtils.str2loc(hub.getConfig().getString("spawn"));
        spawn.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());

    }

    @Override
    public void onDisable() {
        //NULL
    }

    @Override
    public void onLogin(Player player) {
        log(Level.INFO, "Handling login from '" + player.getUniqueId() + "'");

        hub.getScheduledExecutorService().execute(() -> hub.getServer().getScheduler().runTask(hub, () -> {
            InventoryUtils.cleanPlayer(player);
            Titles.sendTabTitle(player,
                    ChatColor.GOLD + "" + ChatColor.BOLD + ChatColor.ITALIC + "Minaria" + ChatColor.RESET + ChatColor.BOLD + ChatColor.GRAY + " - " + ChatColor.RESET + ChatColor.BOLD + ChatColor.YELLOW + "OP Prison\n",
                    "\n" +
                          ChatColor.WHITE + "Une question ? Ou bien juste vous êtes curieux,\n" +
                          ChatColor.WHITE + "vous pouvez faire " + ChatColor.AQUA + "/help " + ChatColor.WHITE + "ou " + ChatColor.AQUA + "/aide" + ChatColor.WHITE + ".\n" +
                          "\n" +
                          ChatColor.YELLOW + "play.minaria.fr"
            );

            player.setGameMode(GameMode.ADVENTURE);
            player.setWalkSpeed(WALK_SPEED);
            player.setFlySpeed(FLY_SPEED);
            player.setAllowFlight(false);
            player.setFoodLevel(20);
            player.setHealth(20.0D);
            player.teleport(spawn);
            
            staticInventory.setInventoryToPlayer(player);
        }));
    }

    @Override
    public void onLogout(Player player) {

    }

    public StaticInventory getStaticInventory() {
        return staticInventory;
    }

    public Location getSpawn() {
        return spawn;
    }
}

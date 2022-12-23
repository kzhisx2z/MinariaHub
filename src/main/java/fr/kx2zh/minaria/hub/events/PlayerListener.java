package fr.kx2zh.minaria.hub.events;

import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final Hub hub;

    public PlayerListener(Hub hub) {
        this.hub = hub;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            if (event.getEntity() instanceof Player) {
                Titles.sendTitle((Player) event.getEntity(), 20, 75, 20, ChatColor.YELLOW + "Ou voullez-vous allez ?", null);
            }

            event.getEntity().teleport(hub.getPlayerManager().getSpawn());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        hub.getEventBus().onLogin(event.getPlayer());
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (!event.getWhoClicked().isOp()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItem();

        if(itemStack == null) return;

        hub.getServer().getScheduler().runTaskAsynchronously(hub, () -> hub.getPlayerManager().getStaticInventory().doInteraction(player, itemStack));
    }

}

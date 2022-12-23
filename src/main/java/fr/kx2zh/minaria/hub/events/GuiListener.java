package fr.kx2zh.minaria.hub.events;

import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.gui.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GuiListener implements Listener {

    private final Hub hub;

    public GuiListener(Hub hub) {
        this.hub = hub;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getCurrentItem() != null) {
            final Player player = (Player) event.getWhoClicked();
            final ItemStack item = event.getCurrentItem();
            final AbstractGui gui = (AbstractGui) hub.getGuiManager().getPlayerGui(player);

            if (event.getClickedInventory() instanceof PlayerInventory) {
                hub.getPlayerManager().getStaticInventory().doInteraction(player, item);
                return;
            }

            if (gui != null) {
                final String action = gui.getAction(event.getSlot());

                if (action != null) {
                    gui.onClick(player, item, action, event.getClick(), event);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (hub.getGuiManager().getPlayerGui(event.getPlayer()) != null) {
            hub.getGuiManager().removeClosedGui((Player) event.getPlayer());
        }
    }
}

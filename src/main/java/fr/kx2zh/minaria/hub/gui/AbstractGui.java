package fr.kx2zh.minaria.hub.gui;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.utils.NumberUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractGui extends fr.kx2zh.minaria.api.gui.AbstractGui {

    protected final Hub hub;

    public AbstractGui(Hub hub) {
        this.hub = hub;
    }

    protected static ItemStack getBackIcon() {
        final ItemStack itemStack = new ItemStack(Material.EMERALD, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.GREEN + "« Retour");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    protected static ItemStack getCoinsIcon(Player player) {
        final long coins = MinariaAPI.get().getPlayerDataManager().getPlayerData(player.getUniqueId()).getCoins();

        final ItemStack itemStack = new ItemStack(Material.GOLD_INGOT, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ChatColor.GOLD + "Vous avez " + NumberUtils.format(coins) + " pièces.");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    protected int getSlot(String action) {
        for (int slot : actions.keySet()) {
            if(actions.get(slot).equals(action)) {
                return slot;
            }
        }

        return 0;
    }
}

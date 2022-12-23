package fr.kx2zh.minaria.hub.common.players;

import fr.kx2zh.minaria.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StaticInventory {

    private final Hub hub;
    private final Map<Integer, ItemStack> items = new HashMap<>();

    StaticInventory(Hub hub) {
        this.hub = hub;

        items.put(0, buildItemStack(Material.COMPASS, 1, 0, createTitle("Menu principal"), null));
        items.put(1, buildItemStack(Material.SKULL_ITEM, 1, 3, createTitle("Profil"), null));
        items.put(8, buildItemStack(Material.GOLD_INGOT, 1, 0, createTitle("Boutique"), null));
    }

    public void setInventoryToPlayer(Player player) {
        for (int slot : items.keySet()) {
            if (items.get(slot).getType() == Material.SKULL_ITEM) {
                final SkullMeta meta = (SkullMeta) this.items.get(slot).getItemMeta();
                meta.setOwner(player.getName());
                items.get(slot).setItemMeta(meta);
            }
            player.getInventory().setItem(slot, items.get(slot));
        }
    }

    public void doInteraction(Player player, ItemStack itemStack) {

        switch (itemStack.getType()) {
            case COMPASS:
                player.sendMessage("Bonjour");
                break;

            default: break;
        }

    }

    private static String createTitle(String text) {
        return ChatColor.GOLD + "" + ChatColor.BOLD + text + ChatColor.RESET + "" + ChatColor.GRAY + " (Clic-droit)";
    }

    private static ItemStack buildItemStack(Material material, int quantity, int data, String name, String[] lores) {
        final ItemStack itemStack = new ItemStack(material, quantity, (short) data);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);

        if (lores != null) {
            itemMeta.setLore(Arrays.asList(lores));
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}

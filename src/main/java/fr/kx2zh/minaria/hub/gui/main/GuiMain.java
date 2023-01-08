package fr.kx2zh.minaria.hub.gui.main;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiMain extends AbstractGui {

    public GuiMain(Hub hub) {
        super(hub);
    }

    @Override
    public void display(Player player) {
        inventory = hub.getServer().createInventory(null, 45, ChatColor.GOLD + "" + ChatColor.BOLD + "Menu Principal");

        setSlotData(ChatColor.GOLD + "FFA-RUSH", Material.ENDER_STONE, 13, makeButtonLore(new String[] {"Retournez à l'ancien temps..."}, false, true), "ffa_rush");

        fillBorder(inventory, stainedGlassPane());

        hub.getServer().getScheduler().runTask(hub, () -> player.openInventory(inventory));
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, InventoryClickEvent event) {
        switch (action) {
            case "ffa_rush":
                MinariaAPI.get().getPlayerDataManager().kickFromNetwork(player, "Salut");
                break;

            default: break;

        }
    }

    private static ItemStack stainedGlassPane() {
        final ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static String[] makeButtonLore(String[] description, boolean clickOpen, boolean clickTeleport) {
        final List<String> lore = new ArrayList<>();

        if (description != null) {
            for (String s : description) {
                lore.add(ChatColor.WHITE + s);
            }

            lore.add("");
        }

        if (clickOpen) {
            lore.add(Hub.SECONDARY_COLOR + "\u25B6 Cliquez pour ouvrir le menu");
        }

        if (clickTeleport) {
            lore.add(Hub.SECONDARY_COLOR + "\u25B6 Cliquez pour être téléporté");
        }

        return lore.toArray(new String[] {});
    }


}

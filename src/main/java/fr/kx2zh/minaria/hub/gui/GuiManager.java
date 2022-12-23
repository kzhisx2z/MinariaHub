package fr.kx2zh.minaria.hub.gui;

import fr.kx2zh.minaria.api.gui.AbstractGui;
import fr.kx2zh.minaria.api.gui.IGuiManager;
import fr.kx2zh.minaria.hub.Hub;
import fr.kx2zh.minaria.hub.common.managers.AbstractManager;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GuiManager extends AbstractManager implements IGuiManager {

    private final ConcurrentMap<UUID, AbstractGui> playersGui = new ConcurrentHashMap<>();

    public GuiManager(Hub hub) {
        super(hub);
    }

    @Override
    public void openGui(Player player, AbstractGui abstractGui) {
        if(playersGui.containsKey(player.getUniqueId())) {
            player.closeInventory();
            playersGui.remove(player.getUniqueId());
        }

        playersGui.put(player.getUniqueId(), abstractGui);
        abstractGui.display(player);
    }

    @Override
    public void closeGui(Player player) {
        if(playersGui.containsKey(player.getUniqueId())) {
            player.closeInventory();
            playersGui.remove(player.getUniqueId());
        }
    }

    @Override
    public void removeClosedGui(Player player) {
        playersGui.remove(player.getUniqueId());
    }

    @Override
    public AbstractGui getPlayerGui(HumanEntity humanEntity) {
        return getPlayerGui(humanEntity.getUniqueId());
    }

    @Override
    public AbstractGui getPlayerGui(UUID uuid) {
        return playersGui.getOrDefault(uuid, null);
    }

    @Override
    public ConcurrentHashMap<UUID, AbstractGui> getPlayerGui() {
        return (ConcurrentHashMap<UUID, AbstractGui>) playersGui;
    }

    @Override
    public void onDisable() {
        hub.getServer().getOnlinePlayers().forEach(this::onLogout);
    }

    @Override
    public void onLogin(Player player) {
        // ///
    }

    @Override
    public void onLogout(Player player) {
        if(playersGui.containsKey(player.getUniqueId())) {
            playersGui.get(player.getUniqueId()).onClose(player);
            playersGui.remove(player.getUniqueId());
        }
    }
}

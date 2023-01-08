package fr.kx2zh.minaria.hub.events;

import fr.kx2zh.minaria.api.MinariaAPI;
import fr.kx2zh.minaria.hub.Hub;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DoubleJumpListener implements Listener {

    private final List<UUID> allowed = new ArrayList<>();
    private final Hub hub;

    public DoubleJumpListener(Hub hub) {
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if(player.getGameMode() != GameMode.ADVENTURE) return;
        if(player.getAllowFlight()) return;

        if(((LivingEntity) player).isOnGround() && MinariaAPI.get().getPermissionManager().hasPermission(player, "hub.doublejump")) {
            player.setAllowFlight(true);
            allowed.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if(!allowed.contains(player.getUniqueId())) return;

        allowed.remove(player.getUniqueId());
        event.setCancelled(true);

        player.setVelocity(player.getLocation().getDirection().multiply(1.6D).setY(1.0D));
        player.playSound(event.getPlayer().getLocation(), Sound.EXPLODE, 1.0F, 1.0F);

        for (int i = 0; i < 20; i++) {
            player.getWorld().playEffect(player.getLocation().subtract(0.0F, 0.20F, 0.0F), Effect.CLOUD, 2);
            //ParticleEffect.CLOUD.display(0.5F, 0.15F, 0.5F, 0.25F, 20, player.getLocation().subtract(0.0F, 0.20F, 0.0F));
        }

        player.setAllowFlight(false);

    }
}

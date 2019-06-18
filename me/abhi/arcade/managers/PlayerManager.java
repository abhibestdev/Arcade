package me.abhi.arcade.managers;

import me.abhi.arcade.games.Game;
import me.abhi.arcade.manager.Manager;
import me.abhi.arcade.manager.ManagerHandler;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager extends Manager {

    private Map<UUID, Game> regionMap;

    public PlayerManager(ManagerHandler managerHandler) {
        super(managerHandler);
        regionMap = new HashMap<>();
    }

    public void resetPlayer(Player player) {
        new BukkitRunnable() {
            public void run() {
                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.updateInventory();
                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potionEffect.getType());
                }
                player.setFireTicks(0);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setGameMode(GameMode.SURVIVAL);
                player.setFlying(false);
                player.setAllowFlight(false);
            }
        }.runTask(this.managerHandler.getPlugin());
    }

    public Map<UUID, Game> getRegionMap() {
        return regionMap;
    }
}

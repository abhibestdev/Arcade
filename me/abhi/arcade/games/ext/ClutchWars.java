package me.abhi.arcade.games.ext;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.util.ItemBuilder;
import me.abhi.arcade.util.LocationUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ClutchWars extends Game {

    private int ticks;

    public ClutchWars(Arcade plugin) {
        super(plugin, "ClutchWars", "Clutch Wars", "&8[&6Clutch Wars&8]", false, true, LocationUtil.getLocationFromString(plugin.getConfig().getString("spawn.clutchwars.location")));
    }

    @Override
    public void start() {
        new BukkitRunnable() {
            public void run() {
                ticks += 1;
                if (ticks >= 150 && getPlugin().getManagerHandler().getGameManager().getGameState() == GameState.GRACE_PERIOD) {
                    getPlugin().getServer().broadcastMessage(Messages.GRACE_PERIOD_ENDED.getMessage());
                    getPlugin().getManagerHandler().getGameManager().setGameState(GameState.STARTED);
                    this.cancel();
                    ticks = 0;
                }
            }
        }.runTaskTimerAsynchronously(this.getPlugin(), 1L, 1L);
        ItemStack stick = new ItemBuilder(Material.STICK).addEnchant(Enchantment.KNOCKBACK, 2).addEnchant(Enchantment.DIG_SPEED, 5).setUnbreakable(true).toItemStack();
        ItemStack enderpearls = new ItemStack(Material.ENDER_PEARL, 16);
        for (UUID uuid : this.getPlugin().getManagerHandler().getGameManager().getAlive()) {
            Player player = this.getPlugin().getServer().getPlayer(uuid);
            player.teleport(getSpawn());
            player.getInventory().setItem(0, stick);
            player.getInventory().setItem(1, enderpearls);
            player.updateInventory();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}

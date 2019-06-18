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

public class Spleef extends Game {

    private int ticks;

    public Spleef(Arcade plugin) {
        super(plugin, "Spleef", "Spleef", "&8[&6Spleef&8]", true, true, LocationUtil.getLocationFromString(plugin.getConfig().getString("spawn.spleef.location")));
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
        ItemStack shovel = new ItemBuilder(Material.DIAMOND_SPADE).addEnchant(Enchantment.DIG_SPEED, 5).setUnbreakable(true).toItemStack();
        for (UUID uuid : this.getPlugin().getManagerHandler().getGameManager().getAlive()) {
            Player player = this.getPlugin().getServer().getPlayer(uuid);
            player.teleport(getSpawn());
            player.getInventory().setItem(0, shovel);
            player.updateInventory();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

}

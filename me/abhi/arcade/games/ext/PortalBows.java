package me.abhi.arcade.games.ext;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.util.ItemBuilder;
import me.abhi.arcade.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PortalBows extends Game {

    private int ticks;

    public PortalBows(Arcade plugin) {
        super(plugin, "PortalBows", "Portal Bows", "&8[&6Portal Bows&8]", false, true, LocationUtil.getLocationFromString(plugin.getConfig().getString("spawn.portalbows.location")));
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
        ItemStack teleportBow = new ItemBuilder(Material.BOW).setName(ChatColor.AQUA + "Teleport Bow").addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.ARROW_KNOCKBACK, 2).setUnbreakable(true).toItemStack();
        ItemStack regularBow = new ItemBuilder(Material.BOW).setName(ChatColor.GREEN + "Regular Bow").addEnchant(Enchantment.ARROW_INFINITE, 1).addEnchant(Enchantment.ARROW_KNOCKBACK, 2).setUnbreakable(true).toItemStack();
        ItemStack arrow = new ItemStack(Material.ARROW);
        for (UUID uuid : this.getPlugin().getManagerHandler().getGameManager().getAlive()) {
            Player player = this.getPlugin().getServer().getPlayer(uuid);
            player.teleport(getSpawn());
            player.getInventory().setItem(0, teleportBow);
            player.getInventory().setItem(1, regularBow);
            player.getInventory().setItem(8, arrow);
            player.updateInventory();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}

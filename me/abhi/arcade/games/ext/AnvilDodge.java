package me.abhi.arcade.games.ext;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.util.Cuboid;
import me.abhi.arcade.util.LocationUtil;
import me.abhi.arcade.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class AnvilDodge extends Game {

    private boolean fell = false;
    private int percent = 10;
    private int ticks;
    private Cuboid cuboid;

    public AnvilDodge(Arcade plugin) {
        super(plugin, "AnvilDodge", "Anvil Dodge", "&8[&6Anvil Dodge&8]", false, false, LocationUtil.getLocationFromString(plugin.getConfig().getString("spawn.anvildodge.location")));
    }

    @Override
    public void start() {
        Location locationOne = LocationUtil.getLocationFromString(getPlugin().getConfig().getString("region." + getSimpleName() + ".1"));
        Location locationTwo = LocationUtil.getLocationFromString(getPlugin().getConfig().getString("region." + getSimpleName() + ".2"));
        cuboid = new Cuboid(locationOne, locationTwo);
        for (UUID uuid : this.getPlugin().getManagerHandler().getGameManager().getAlive()) {
            Player player = this.getPlugin().getServer().getPlayer(uuid);
            player.teleport(getSpawn());
        }
        new BukkitRunnable() {
            public void run() {
                ticks += 1;
                if (ticks >= 150 && getPlugin().getManagerHandler().getGameManager().getGameState() == GameState.GRACE_PERIOD) {
                    getPlugin().getServer().broadcastMessage(Messages.GRACE_PERIOD_ENDED.getMessage());
                    getPlugin().getManagerHandler().getGameManager().setGameState(GameState.STARTED);
                    this.cancel();
                    ticks = 0;
                    new BukkitRunnable() {
                        public void run() {
                            if (getPlugin().getManagerHandler().getGameManager().getGameState() != GameState.STARTED && getPlugin().getManagerHandler().getGameManager().getGameState() != GameState.GRACE_PERIOD) {
                                this.cancel();
                            } else {
                                if (!fell) {
                                    Location higherOne = new Location(cuboid.getWorld(), cuboid.getLocation1().getX(), cuboid.getUpperY() + 60, cuboid.getLocation1().getZ());
                                    Location higherTwo = new Location(cuboid.getWorld(), cuboid.getLocation2().getX(), cuboid.getUpperY() + 60, cuboid.getLocation2().getZ());
                                    Cuboid anvilArea = new Cuboid(higherOne, higherTwo);
                                    getPlugin().getServer().broadcastMessage(Messages.ANVIL_DODGE_PERCENT.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', getPrefix())).replace("%percent%", percent + "%"));
                                    for (Block block : Util.getPercantage(anvilArea.getBlocks(), percent)) {
                                        if (block.getType() == Material.AIR) {
                                            block.setType(Material.ANVIL);
                                        }
                                    }
                                    fell = true;
                                }
                            }
                            for (Block block : cuboid.getBlocks()) {
                                block.setType(Material.AIR);
                            }
                            ticks += 1;
                            if (ticks % 150 == 0) {
                                fell = false;
                                if (percent + 5 <= 99) {
                                    percent += 5;
                                } else {
                                    percent = 99;
                                }
                            }
                        }
                    }.runTaskTimer(getPlugin(), 1L, 1L);
                }
            }
        }.runTaskTimerAsynchronously(this.getPlugin(), 1L, 1L);
    }

    @Override
    public void stop() {
        fell = false;
        percent = 10;
        for (Block block : cuboid.getBlocks()) {
            if (block.getType() == Material.ANVIL) {
                block.setType(Material.AIR);
            }
        }
    }
}

package me.abhi.arcade.games.ext;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.util.LocationUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Sumo extends Game {

    private int ticks;

    public Sumo(Arcade plugin) {
        super(plugin, "Sumo", "Sumo", "&8[&6Sumo&8]", false, true, LocationUtil.getLocationFromString(plugin.getConfig().getString("spawn.sumo.location")));
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
        for (UUID uuid : this.getPlugin().getManagerHandler().getGameManager().getAlive()) {
            Player player = this.getPlugin().getServer().getPlayer(uuid);
            player.teleport(getSpawn());
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}

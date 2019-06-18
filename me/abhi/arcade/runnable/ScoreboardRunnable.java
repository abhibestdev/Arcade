package me.abhi.arcade.runnable;

import me.abhi.arcade.Arcade;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardRunnable extends BukkitRunnable {

    private Arcade plugin;

    public ScoreboardRunnable(Arcade plugin) {
        this.plugin = plugin;
    }

    public void run() {
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.plugin.getManagerHandler().getScoreboardManager().update(player);
        }
    }
}

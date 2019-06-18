package me.abhi.arcade.runnable;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.events.GameStartEvent;
import me.abhi.arcade.games.Game;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class GameRunnable extends BukkitRunnable {

    private Arcade plugin;

    public GameRunnable(Arcade plugin) {
        this.plugin = plugin;
    }

    public void run() {
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.LOBBY && this.plugin.getServer().getOnlinePlayers().size() >= 2) {
            Game game = this.plugin.getManagerHandler().getGameManager().getGameList().get(new Random().nextInt(this.plugin.getManagerHandler().getGameManager().getGameList().size()));
            this.plugin.getServer().getPluginManager().callEvent(new GameStartEvent(game, 0));
        }
    }
}

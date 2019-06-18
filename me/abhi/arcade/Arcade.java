package me.abhi.arcade;

import me.abhi.arcade.commands.*;
import me.abhi.arcade.listener.GameListener;
import me.abhi.arcade.listener.PlayerListener;
import me.abhi.arcade.manager.ManagerHandler;
import me.abhi.arcade.runnable.GameRunnable;
import me.abhi.arcade.runnable.ScoreboardRunnable;
import me.abhi.arcade.util.ScoreHelper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Arcade extends JavaPlugin {

    private static Arcade instance;
    private ManagerHandler managerHandler;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        registerManagers();
        registerCommands();
        registerListeners();
        registerPlayers();
        registerRunnables();
    }

    private void registerManagers() {
        managerHandler = new ManagerHandler(this);
    }

    private void registerCommands() {
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("vote").setExecutor(new VoteCommand(this));
        getCommand("setregion").setExecutor(new SetRegionCommand(this));
        getCommand("devstart").setExecutor(new DevStartCommand(this));
        getCommand("pause").setExecutor(new PauseCommand(this));
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new GameListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerPlayers() {
        for (Player player : getServer().getOnlinePlayers()) {
            ScoreHelper.createScore(player);
            this.managerHandler.getPlayerManager().resetPlayer(player);
            player.teleport(this.managerHandler.getServerManager().getSpawn());
        }
    }

    private void registerRunnables() {
        new ScoreboardRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
        new GameRunnable(this).runTaskTimerAsynchronously(this, 0L, 0L);
    }

    public static Arcade getInstance() {
        return instance;
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}

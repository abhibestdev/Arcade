package me.abhi.arcade.manager;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.managers.GameManager;
import me.abhi.arcade.managers.PlayerManager;
import me.abhi.arcade.managers.ScoreboardManager;
import me.abhi.arcade.managers.ServerManager;

public class ManagerHandler {

    private Arcade plugin;
    private GameManager gameManager;
    private ScoreboardManager scoreboardManager;
    private ServerManager serverManager;
    private PlayerManager playerManager;

    public ManagerHandler(Arcade plugin) {
        this.plugin = plugin;
        registerManagers();
    }

    private void registerManagers() {
        gameManager = new GameManager(this);
        scoreboardManager = new ScoreboardManager(this);
        serverManager = new ServerManager(this);
        playerManager = new PlayerManager(this);
    }

    public Arcade getPlugin() {
        return plugin;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}

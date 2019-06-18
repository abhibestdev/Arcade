package me.abhi.arcade.managers;

import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.games.ext.*;
import me.abhi.arcade.manager.Manager;
import me.abhi.arcade.manager.ManagerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager extends Manager {

    private GameState gameState;
    private List<Game> gameList;
    private Game currentGame;
    private int waitTime;
    private List<UUID> alive;

    public GameManager(ManagerHandler managerHandler) {
        super(managerHandler);
        gameState = GameState.LOBBY;
        gameList = new ArrayList<>();
        alive = new ArrayList<>();
        loadGames();
    }

    private void loadGames() {
        addGame(new Spleef(managerHandler.getPlugin()));
        addGame(new PortalBows(managerHandler.getPlugin()));
        addGame(new Sumo(managerHandler.getPlugin()));
        addGame(new ClutchWars(managerHandler.getPlugin()));
        addGame(new AnvilDodge(managerHandler.getPlugin()));
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void addGame(Game game) {
        gameList.add(game);
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public Game getGame(String name) {
        for (Game game : gameList) {
            if (game.getSimpleName().equalsIgnoreCase(name)) {
                return game;
            }
        }
        return null;
    }

    public boolean gameExists(String name) {
        for (Game game : gameList) {
            if (game.getSimpleName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public List<UUID> getAlive() {
        return alive;
    }
}

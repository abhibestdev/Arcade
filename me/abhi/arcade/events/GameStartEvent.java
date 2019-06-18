package me.abhi.arcade.events;

import me.abhi.arcade.games.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Game game;
    private int votes;

    public GameStartEvent(Game game, int votes) {
        this.game = game;
        this.votes = votes;
    }

    public Game getGame() {
        return game;
    }

    public int getVotes() {
        return votes;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

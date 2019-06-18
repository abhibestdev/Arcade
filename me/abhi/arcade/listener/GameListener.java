package me.abhi.arcade.listener;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.changedblock.ChangedBlock;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.events.GameStartEvent;
import me.abhi.arcade.events.GameStopEvent;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.util.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameListener implements Listener {

    private Arcade plugin;

    public GameListener(Arcade plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Game game = event.getGame();
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTING || this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.PAUSED) {
            return;
        }
        this.plugin.getManagerHandler().getServerManager().getVoteGames().clear();
        this.plugin.getManagerHandler().getServerManager().getVotes().clear();
        this.plugin.getManagerHandler().getServerManager().getVoted().clear();
        this.plugin.getManagerHandler().getGameManager().setGameState(GameState.STARTING);
        this.plugin.getServer().broadcastMessage(Messages.GAME_WON_VOTE.getMessage().replace("%game%", game.getName()).replace("%votes%", String.valueOf(event.getVotes())));
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (!this.plugin.getManagerHandler().getGameManager().getAlive().contains(player.getUniqueId())) {
                this.plugin.getManagerHandler().getGameManager().getAlive().add(player.getUniqueId());
                this.plugin.getManagerHandler().getPlayerManager().resetPlayer(player);
            }
        }
        this.plugin.getManagerHandler().getGameManager().setCurrentGame(game);
        this.plugin.getManagerHandler().getGameManager().setWaitTime(10);
        new BukkitRunnable() {
            public void run() {
                if (plugin.getManagerHandler().getGameManager().getGameState() != GameState.STARTING) {
                    this.cancel();
                }
                if (plugin.getManagerHandler().getGameManager().getWaitTime() <= 0) {
                    this.cancel();
                    game.start();
                    new BukkitRunnable() {
                        public void run() {
                            game.getSpawn().getChunk().load();
                        }
                    }.runTask(plugin);
                    plugin.getManagerHandler().getGameManager().setGameState(GameState.GRACE_PERIOD);
                    plugin.getServer().broadcastMessage(Messages.GAME_STARTS_NOW.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', game.getPrefix())));
                    plugin.getServer().broadcastMessage(Messages.GRACE_PERIOD.getMessage());
                } else {
                    plugin.getServer().broadcastMessage(Messages.GAME_STARTING_IN.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', game.getPrefix())).replace("%seconds%", String.valueOf(plugin.getManagerHandler().getGameManager().getWaitTime())));
                    plugin.getManagerHandler().getGameManager().setWaitTime(plugin.getManagerHandler().getGameManager().getWaitTime() - 1);
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 20L, 20L);
    }

    @EventHandler
    public void onGameStop(GameStopEvent event) {
        Game game = event.getGame();
        game.stop();
        String winnerName = event.getWinner().getName();
        this.plugin.getManagerHandler().getServerManager().getVoteGames().clear();
        this.plugin.getManagerHandler().getGameManager().setGameState(GameState.VOTING);
        if (game.isBreakBlocks()) {
            for (ChangedBlock changedBlock : plugin.getManagerHandler().getServerManager().getChangedBlockList()) {
                changedBlock.getBlock().setType(changedBlock.getMaterial());
                changedBlock.getBlock().setData(changedBlock.getData());
            }
            plugin.getManagerHandler().getServerManager().getChangedBlockList().clear();
        }
        List<Game> gameList = new ArrayList<>();
        for (Game games : this.plugin.getManagerHandler().getGameManager().getGameList()) {
            if (games != game) {
                gameList.add(games);
            }
        }
        Collections.shuffle(gameList);
        String separator = ChatColor.GOLD + "========== " + ChatColor.BOLD + "CLICK TO VOTE" + ChatColor.GOLD + " ==========";
        TextComponent gameOne = new TextComponent(ChatColor.DARK_GREEN + gameList.get(0).getName());
        gameOne.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to vote for " + gameList.get(0).getName() + ".").create()));
        gameOne.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote " + gameList.get(0).getSimpleName()));
        TextComponent gameTwo = new TextComponent(ChatColor.DARK_GREEN + gameList.get(1).getName());
        gameTwo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to vote for " + gameList.get(1).getName() + ".").create()));
        gameTwo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote " + gameList.get(1).getSimpleName()));
        TextComponent gameThree = new TextComponent(ChatColor.DARK_GREEN + gameList.get(2).getName());
        gameThree.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to vote for " + gameList.get(2).getName() + ".").create()));
        gameThree.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote " + gameList.get(2).getSimpleName()));
        for (int i = 0; i <= 2; i++) {
            this.plugin.getManagerHandler().getServerManager().getVoteGames().add(gameList.get(i));
        }
        this.plugin.getServer().broadcastMessage("");
        this.plugin.getServer().broadcastMessage(Messages.PLAYER_WON_GAME.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', game.getPrefix())).replace("%player%", winnerName));
        this.plugin.getServer().broadcastMessage("");
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.plugin.getManagerHandler().getPlayerManager().resetPlayer(player);
            player.teleport(this.plugin.getManagerHandler().getServerManager().getSpawn());
            player.sendMessage(separator);
            player.sendMessage("");
            player.sendMessage(gameOne);
            player.sendMessage("");
            player.sendMessage(gameTwo);
            player.sendMessage("");
            player.sendMessage(gameThree);
            player.sendMessage("");
            player.sendMessage(separator);
        }
        this.plugin.getManagerHandler().getGameManager().setWaitTime(20);
        new BukkitRunnable() {
            public void run() {
                if (plugin.getManagerHandler().getGameManager().getWaitTime() <= 0) {
                    this.cancel();
                    List<Map.Entry<Game, Integer>> greatest = Util.findGreatest(plugin.getManagerHandler().getServerManager().getVotes(), 1);
                    if (greatest.size() > 0) {
                        Game highestVoted = greatest.get(0).getKey();
                        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(highestVoted, greatest.get(0).getValue()));
                    } else {
                        Game randomPick = plugin.getManagerHandler().getServerManager().getVoteGames().get(new Random().nextInt(plugin.getManagerHandler().getServerManager().getVoteGames().size()));
                        plugin.getServer().getPluginManager().callEvent(new GameStartEvent(randomPick, 0));
                    }
                } else {
                    plugin.getManagerHandler().getGameManager().setWaitTime(plugin.getManagerHandler().getGameManager().getWaitTime() - 1);
                }
                if (plugin.getManagerHandler().getGameManager().getGameState() != GameState.VOTING) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 20L, 20L);
    }
}

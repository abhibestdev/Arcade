package me.abhi.arcade.commands;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.games.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand implements CommandExecutor {

    private Arcade plugin;

    public VoteCommand(Arcade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <game>");
            return true;
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.VOTING) {
            sender.sendMessage(Messages.NOT_VOTING_PERIOD.getMessage());
            return true;
        }
        if (!this.plugin.getManagerHandler().getGameManager().gameExists(args[0])) {
            sender.sendMessage(Messages.GAME_DOESNT_EXIST.getMessage());
            return true;
        }
        Game game = this.plugin.getManagerHandler().getGameManager().getGame(args[0]);
        if (!this.plugin.getManagerHandler().getServerManager().getVoteGames().contains(game)) {
            sender.sendMessage(Messages.CANT_VOTE_FOR_GAME.getMessage());
            return true;
        }
        if (this.plugin.getManagerHandler().getServerManager().getVoted().contains(player.getUniqueId())) {
            sender.sendMessage(Messages.ALREADY_VOTED.getMessage());
            return true;
        }
        this.plugin.getManagerHandler().getServerManager().getVoted().add(player.getUniqueId());
        this.plugin.getManagerHandler().getServerManager().getVotes().put(game, this.plugin.getManagerHandler().getServerManager().getVotes().getOrDefault(game, 0) + 1);
        sender.sendMessage(Messages.VOTED_FOR_GAME.getMessage().replace("%game%", game.getName()));
        return true;
    }
}

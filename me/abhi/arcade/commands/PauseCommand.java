package me.abhi.arcade.commands;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PauseCommand implements CommandExecutor {

    private Arcade plugin;

    public PauseCommand(Arcade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission("arcade.command.pause")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED) {
            sender.sendMessage(ChatColor.RED + "You can only pause the game in the lobby.");
            return true;
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.PAUSED) {
            sender.sendMessage(ChatColor.GREEN + "Game unpaused.");
            this.plugin.getManagerHandler().getGameManager().setGameState(GameState.LOBBY);
            return true;
        }
        this.plugin.getManagerHandler().getGameManager().setGameState(GameState.PAUSED);
        sender.sendMessage(ChatColor.GREEN + "Game paused.");
        return true;
    }
}

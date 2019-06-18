package me.abhi.arcade.commands;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.events.GameStartEvent;
import me.abhi.arcade.games.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DevStartCommand implements CommandExecutor {

    private Arcade plugin;

    public DevStartCommand(Arcade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("arcade.command.devstart")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <game>");
            sender.sendMessage(ChatColor.RED + "This is a development command that will only be used to test gamemodes with only one player.");
            return true;
        }
        if (this.plugin.getServer().getOnlinePlayers().size() != 1) {
            sender.sendMessage(ChatColor.RED + "This is a development command that will only be used to test gamemodes with only one player.");
            return true;
        }
        if (!this.plugin.getManagerHandler().getGameManager().gameExists(args[0])) {
            sender.sendMessage(Messages.GAME_DOESNT_EXIST.getMessage());
            return true;
        }
        Game game = this.plugin.getManagerHandler().getGameManager().getGame(args[0]);
        this.plugin.getServer().getPluginManager().callEvent(new GameStartEvent(game, 0));
        return true;
    }
}

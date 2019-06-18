package me.abhi.arcade.commands;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.games.Game;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRegionCommand implements CommandExecutor {

    private Arcade plugin;

    public SetRegionCommand(Arcade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("arcade.command.setregion")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " game <game>");
            return true;
        }
        Player player = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "game": {
                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " game <game>");
                    return true;
                }
                String name = args[1];
                if (!this.plugin.getManagerHandler().getGameManager().gameExists(name)) {
                    sender.sendMessage(Messages.GAME_DOESNT_EXIST.getMessage());
                    return true;
                }
                Game game = this.plugin.getManagerHandler().getGameManager().getGame(name);
                if (this.plugin.getManagerHandler().getPlayerManager().getRegionMap().containsKey(player.getUniqueId())) {
                    this.plugin.getManagerHandler().getPlayerManager().getRegionMap().remove(player.getUniqueId());
                    sender.sendMessage(Messages.EXITED_REGION_SELECTION.getMessage());
                    return true;
                }
                sender.sendMessage(Messages.SELECTING_REGION_FOR.getMessage().replace("%game%", name.toUpperCase()));
                this.plugin.getManagerHandler().getPlayerManager().getRegionMap().put(player.getUniqueId(), game);
                break;
            }
        }
        return true;
    }
}

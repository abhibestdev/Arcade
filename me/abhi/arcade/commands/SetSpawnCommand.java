package me.abhi.arcade.commands;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private Arcade plugin;

    public SetSpawnCommand(Arcade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        if (!sender.hasPermission("arcade.command.setspawn")) {
            sender.sendMessage(Messages.NO_PERMISSION.getMessage());
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + commandLabel + " <lobby:game> <possible argument>");
            return true;
        }
        Player player = (Player) sender;
        switch (args[0].toLowerCase()) {
            case "lobby": {
                this.plugin.getConfig().set("spawn.lobby.location", LocationUtil.getStringFromLocation(player.getLocation()));
                this.plugin.saveConfig();
                this.plugin.getManagerHandler().getServerManager().setSpawn(player.getLocation());
                sender.sendMessage(Messages.SPAWN_SET.getMessage().replace("%spawn%", "LOBBY"));
                break;
            }
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
                this.plugin.getConfig().set("spawn." + name.toLowerCase() + ".location", LocationUtil.getStringFromLocation(player.getLocation()));
                this.plugin.saveConfig();
                sender.sendMessage(Messages.SPAWN_SET.getMessage().replace("%spawn%", name.toUpperCase()));
                break;
            }
        }
        return true;
    }
}

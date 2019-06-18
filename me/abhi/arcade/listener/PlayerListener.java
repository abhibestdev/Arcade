package me.abhi.arcade.listener;

import me.abhi.arcade.Arcade;
import me.abhi.arcade.changedblock.ChangedBlock;
import me.abhi.arcade.enums.GameState;
import me.abhi.arcade.enums.Messages;
import me.abhi.arcade.events.GameStopEvent;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.util.LocationUtil;
import me.abhi.arcade.util.ScoreHelper;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    private Arcade plugin;

    public PlayerListener(Arcade plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(Messages.PLAYER_JOINED.getMessage().replace("%player%", event.getPlayer().getName()));
        Player player = event.getPlayer();
        ScoreHelper.createScore(player);
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.LOBBY) {
            this.plugin.getManagerHandler().getPlayerManager().resetPlayer(player);
            this.plugin.getManagerHandler().getServerManager().getSpawn().getChunk().load();
            player.teleport(this.plugin.getManagerHandler().getServerManager().getSpawn());
        } else if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED) {
            player.setGameMode(GameMode.SPECTATOR);
            player.setAllowFlight(true);
            player.setFlying(true);
            this.plugin.getManagerHandler().getGameManager().getCurrentGame().getSpawn().getChunk().load();
            player.teleport(this.plugin.getManagerHandler().getGameManager().getCurrentGame().getSpawn());
        }

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Messages.PLAYER_LEFT.getMessage().replace("%player%", event.getPlayer().getName()));
        Player player = event.getPlayer();
        if (this.plugin.getManagerHandler().getGameManager().getAlive().contains(player.getUniqueId())) {
            this.plugin.getManagerHandler().getGameManager().getAlive().remove(player.getUniqueId());
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.LOBBY && this.plugin.getServer().getOnlinePlayers().size() == 2) {
            this.plugin.getManagerHandler().getGameManager().setGameState(GameState.LOBBY);
            for (Player all : this.plugin.getServer().getOnlinePlayers()) {
                this.plugin.getManagerHandler().getPlayerManager().resetPlayer(all);
                all.teleport(this.plugin.getManagerHandler().getServerManager().getSpawn());
            }
            new BukkitRunnable() {
                public void run() {
                    plugin.getServer().broadcastMessage(" ");
                    plugin.getServer().broadcastMessage(Messages.NOT_ENOUGH_PLAYERS_TO_START.getMessage());
                    plugin.getServer().broadcastMessage(" ");
                    plugin.getManagerHandler().getGameManager().setGameState(GameState.LOBBY);
                }
            }.runTaskLaterAsynchronously(this.plugin, 40L);
        }
        ScoreHelper.removeScore(player);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getTo().getY() <= 15) {
            if (this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.STARTED && this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.GRACE_PERIOD) {
                player.teleport(this.plugin.getManagerHandler().getServerManager().getSpawn());
            }
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.GRACE_PERIOD) {
            if (event.getTo().getY() <= 50) {
                player.teleport(this.plugin.getManagerHandler().getGameManager().getCurrentGame().getSpawn());
            }
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED) {
            Game game = this.plugin.getManagerHandler().getGameManager().getCurrentGame();
            Block block = player.getLocation().clone().subtract(0, 1, 0).getBlock();
            if (block.getType() == Material.BEDROCK && this.plugin.getManagerHandler().getGameManager().getAlive().contains(player.getUniqueId())) {
                this.plugin.getManagerHandler().getGameManager().getAlive().remove(player.getUniqueId());
                player.setGameMode(GameMode.SPECTATOR);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.teleport(this.plugin.getManagerHandler().getGameManager().getCurrentGame().getSpawn());
                this.plugin.getServer().broadcastMessage(Messages.PLAYER_ELIMINATED.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', this.plugin.getManagerHandler().getGameManager().getCurrentGame().getPrefix())).replace("%player%", player.getName()));
                if (this.plugin.getManagerHandler().getGameManager().getAlive().size() == 1) {
                    Player winner = this.plugin.getServer().getPlayer(this.plugin.getManagerHandler().getGameManager().getAlive().get(0));
                    this.plugin.getServer().getPluginManager().callEvent(new GameStopEvent(game, winner));
                }
            }
            if (event.getTo().getY() <= 50 && this.plugin.getManagerHandler().getGameManager().getAlive().contains(player.getUniqueId())) {
                this.plugin.getManagerHandler().getGameManager().getAlive().remove(player.getUniqueId());
                player.setGameMode(GameMode.SPECTATOR);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.teleport(this.plugin.getManagerHandler().getGameManager().getCurrentGame().getSpawn());
                this.plugin.getServer().broadcastMessage(Messages.PLAYER_ELIMINATED.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', this.plugin.getManagerHandler().getGameManager().getCurrentGame().getPrefix())).replace("%player%", player.getName()));
                if (this.plugin.getManagerHandler().getGameManager().getAlive().size() == 1) {
                    Player winner = this.plugin.getServer().getPlayer(this.plugin.getManagerHandler().getGameManager().getAlive().get(0));
                    this.plugin.getServer().getPluginManager().callEvent(new GameStopEvent(game, winner));
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.STARTED && this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.GRACE_PERIOD) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.GRACE_PERIOD) {
            event.setCancelled(true);
        }
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED) {
            Game game = this.plugin.getManagerHandler().getGameManager().getCurrentGame();
            if (game.isBreakBlocks()) {
                event.getBlock().getDrops().clear();
                ChangedBlock changedBlock = new ChangedBlock(event.getBlock(), event.getBlock().getType(), event.getBlock().getData());
                this.plugin.getManagerHandler().getServerManager().addChangedBlock(changedBlock);
                event.setCancelled(true);
                event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation()).setType(Material.AIR);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.VOTING && !player.hasPermission("arcade.staff")) {
            event.setCancelled(true);
            player.sendMessage(Messages.CANT_CHAT_DURING_VOTING.getMessage());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED) {
                Game game = this.plugin.getManagerHandler().getGameManager().getCurrentGame();
                if (!game.isPvp()) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (this.plugin.getManagerHandler().getGameManager().getGameState() != GameState.STARTED) {
            event.setCancelled(true);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED && this.plugin.getManagerHandler().getGameManager().getAlive().contains(player.getUniqueId())) {
                event.setCancelled(true);
                this.plugin.getManagerHandler().getGameManager().getAlive().remove(player.getUniqueId());
                player.setGameMode(GameMode.SPECTATOR);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.teleport(this.plugin.getManagerHandler().getGameManager().getCurrentGame().getSpawn());
                this.plugin.getServer().broadcastMessage(Messages.PLAYER_ELIMINATED.getMessage().replace("%prefix%", ChatColor.translateAlternateColorCodes('&', this.plugin.getManagerHandler().getGameManager().getCurrentGame().getPrefix())).replace("%player%", player.getName()));
                if (this.plugin.getManagerHandler().getGameManager().getAlive().size() == 1) {
                    Player winner = this.plugin.getServer().getPlayer(this.plugin.getManagerHandler().getGameManager().getAlive().get(0));
                    this.plugin.getServer().getPluginManager().callEvent(new GameStopEvent(this.plugin.getManagerHandler().getGameManager().getCurrentGame(), winner));
                }
            }
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        } else if (event.getEntity() instanceof Player) {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            player.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED || this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.GRACE_PERIOD) {
            if (event.getBow().getItemMeta() != null && ChatColor.stripColor(event.getBow().getItemMeta().getDisplayName()).equalsIgnoreCase("Teleport Bow") && event.getProjectile() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getProjectile();
                arrow.setCustomName(ChatColor.GOLD + "Teleport Arrow");
            }
        }
    }

    @EventHandler
    public void onBowHit(ProjectileHitEvent event) {
        if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.STARTED || this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.GRACE_PERIOD) {
            if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Arrow) {
                Player player = (Player) event.getEntity().getShooter();
                Arrow arrow = (Arrow) event.getEntity();
                if (arrow.getCustomName() != null && arrow.getCustomName().equalsIgnoreCase(ChatColor.GOLD + "Teleport Arrow")) {
                    Location teleportLocation = arrow.getLocation();
                    teleportLocation.setPitch(player.getLocation().getPitch());
                    teleportLocation.setYaw(player.getLocation().getYaw());
                    player.teleport(teleportLocation);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (this.plugin.getManagerHandler().getPlayerManager().getRegionMap().containsKey(player.getUniqueId())) {
                event.setCancelled(true);
                Game game = this.plugin.getManagerHandler().getPlayerManager().getRegionMap().get(player.getUniqueId());
                this.plugin.getConfig().set("region." + game.getSimpleName() + ".1", LocationUtil.getStringFromLocation(event.getClickedBlock().getLocation()));
                this.plugin.saveConfig();
                player.sendMessage(Messages.SET_POINT.getMessage().replace("%point%", "1"));
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (this.plugin.getManagerHandler().getPlayerManager().getRegionMap().containsKey(player.getUniqueId())) {
                event.setCancelled(true);
                Game game = this.plugin.getManagerHandler().getPlayerManager().getRegionMap().get(player.getUniqueId());
                this.plugin.getConfig().set("region." + game.getSimpleName() + ".2", LocationUtil.getStringFromLocation(event.getClickedBlock().getLocation()));
                this.plugin.saveConfig();
                player.sendMessage(Messages.SET_POINT.getMessage().replace("%point%", "2"));
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            if (this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.LOBBY || this.plugin.getManagerHandler().getGameManager().getGameState() == GameState.VOTING) {
                event.setCancelled(true);
            }
        }
    }
}

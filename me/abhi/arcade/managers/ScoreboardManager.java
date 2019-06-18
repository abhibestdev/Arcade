package me.abhi.arcade.managers;

import me.abhi.arcade.manager.Manager;
import me.abhi.arcade.manager.ManagerHandler;
import me.abhi.arcade.util.ScoreHelper;
import org.bukkit.entity.Player;

public class ScoreboardManager extends Manager {

    public ScoreboardManager(ManagerHandler managerHandler) {
        super(managerHandler);
    }

    public void update(Player player) {
        if (ScoreHelper.hasScore(player)) {
            ScoreHelper scoreHelper = ScoreHelper.getByPlayer(player);
            scoreHelper.setTitle("&6&lCool&e&lCrafter&7&lClub");
            switch (this.managerHandler.getGameManager().getGameState()) {
                case LOBBY: {
                    scoreHelper.setSlot(15, "&8&m--------------------");
                    scoreHelper.setSlot(14, "&cWaiting for players...");
                    scoreHelper.setSlot(13, "&ePlayers needed: &f" + (2 - this.managerHandler.getPlugin().getServer().getOnlinePlayers().size()));
                    scoreHelper.setSlot(12, "");
                    scoreHelper.setSlot(11, "&7&ocoolcrafter.club");
                    scoreHelper.setSlot(10, "&8&m--------------------");
                    break;
                }
                case GRACE_PERIOD: {
                    scoreHelper.setSlot(15, "&8&m--------------------");
                    scoreHelper.setSlot(14, "&cGrace period...");
                    scoreHelper.setSlot(13, "&eAlive: &f" + this.managerHandler.getPlugin().getServer().getOnlinePlayers().size());
                    scoreHelper.setSlot(12, "");
                    scoreHelper.setSlot(11, "&7&ocoolcrafter.club");
                    scoreHelper.setSlot(10, "&8&m--------------------");
                    break;
                }
                case STARTING: {
                    scoreHelper.setSlot(15, "&8&m--------------------");
                    scoreHelper.setSlot(14, "&eStarting in:&f " + (this.managerHandler.getGameManager().getWaitTime() >= 10 ? 10 : this.managerHandler.getGameManager().getWaitTime() + 1) + "s");
                    scoreHelper.setSlot(13, "&eAlive: &f" + this.managerHandler.getPlugin().getServer().getOnlinePlayers().size());
                    scoreHelper.setSlot(12, "");
                    scoreHelper.setSlot(11, "&7&ocoolcrafter.club");
                    scoreHelper.setSlot(10, "&8&m--------------------");
                    break;
                }
                case STARTED: {
                    scoreHelper.setSlot(15, "&8&m--------------------");
                    scoreHelper.setSlot(14, "&eGame: &f" + this.managerHandler.getGameManager().getCurrentGame().getName());
                    scoreHelper.setSlot(13, "&eAlive: &f" + this.managerHandler.getGameManager().getAlive().size());
                    scoreHelper.setSlot(12, "");
                    scoreHelper.setSlot(11, "&7&ocoolcrafter.club");
                    scoreHelper.setSlot(10, "&8&m--------------------");
                    break;
                }
                case VOTING: {
                    scoreHelper.setSlot(15, "&8&m--------------------");
                    scoreHelper.setSlot(14, "&eVoting ends in: &f" + this.managerHandler.getGameManager().getWaitTime() + "s");
                    scoreHelper.setSlot(13, "&ePlayers: &f" + this.managerHandler.getPlugin().getServer().getOnlinePlayers().size());
                    scoreHelper.setSlot(12, "");
                    scoreHelper.setSlot(11, "&7&ocoolcrafter.club");
                    scoreHelper.setSlot(10, "&8&m--------------------");
                    break;
                }
                case PAUSED: {
                    scoreHelper.setSlot(15, "&8&m--------------------");
                    scoreHelper.setSlot(14, "&cThe game is currently paused.");
                    scoreHelper.setSlot(13, "&ePlayers: &f" + this.managerHandler.getPlugin().getServer().getOnlinePlayers().size());
                    scoreHelper.setSlot(12, "");
                    scoreHelper.setSlot(11, "&7&ocoolcrafter.club");
                    scoreHelper.setSlot(10, "&8&m--------------------");
                    break;
                }
            }
        }
    }
}

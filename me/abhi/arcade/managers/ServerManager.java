package me.abhi.arcade.managers;

import me.abhi.arcade.changedblock.ChangedBlock;
import me.abhi.arcade.games.Game;
import me.abhi.arcade.manager.Manager;
import me.abhi.arcade.manager.ManagerHandler;
import me.abhi.arcade.util.LocationUtil;
import org.bukkit.Location;

import java.util.*;

public class ServerManager extends Manager {

    private List<ChangedBlock> changedBlockList;
    private Location spawn;
    private List<Game> voteGames;
    private Map<Game, Integer> votes;
    private List<UUID> voted;

    public ServerManager(ManagerHandler managerHandler) {
        super(managerHandler);
        changedBlockList = new ArrayList<>();
        spawn = LocationUtil.getLocationFromString(this.managerHandler.getPlugin().getConfig().getString("spawn.lobby.location"));
        voteGames = new ArrayList<>();
        votes = new HashMap();
        voted = new ArrayList<>();
    }

    public List<ChangedBlock> getChangedBlockList() {
        return changedBlockList;
    }

    public void addChangedBlock(ChangedBlock changedBlock) {
        changedBlockList.add(changedBlock);
    }

    public Location getSpawn() {
        return spawn;
    }

    public List<Game> getVoteGames() {
        return voteGames;
    }

    public Map<Game, Integer> getVotes() {
        return votes;
    }

    public List<UUID> getVoted() {
        return voted;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
}

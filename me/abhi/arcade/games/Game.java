package me.abhi.arcade.games;

import me.abhi.arcade.Arcade;
import org.bukkit.Location;

public class Game {

    private Arcade plugin;
    private String simpleName;
    private String name;
    private String prefix;
    private boolean breakBlocks;
    private boolean pvp;
    private Location spawn;

    public Game(Arcade plugin, String simpleName, String name, String prefix, boolean breakBlocks, boolean pvp, Location spawn) {
        this.plugin = plugin;
        this.simpleName = simpleName;
        this.name = name;
        this.prefix = prefix;
        this.breakBlocks = breakBlocks;
        this.pvp = pvp;
        this.spawn = spawn;
    }

    public Arcade getPlugin() {
        return plugin;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isBreakBlocks() {
        return breakBlocks;
    }

    public boolean isPvp() {
        return pvp;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void start() {
    }

    public void stop() {
    }

}

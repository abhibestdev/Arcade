package me.abhi.arcade.changedblock;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class ChangedBlock {

    private Block block;
    private Material material;
    private byte data;

    public ChangedBlock(Block block, Material material, byte data) {
        this.block = block;
        this.material = material;
        this.data = data;
    }

    public Block getBlock() {
        return block;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }
}

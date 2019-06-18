package me.abhi.arcade.util;

import org.bukkit.block.Block;

import java.util.*;

public class Util {

    public static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>>
    findGreatest(Map<K, V> map, int n) {
        Comparator<? super Map.Entry<K, V>> comparator =
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e0, Map.Entry<K, V> e1) {
                        V v0 = e0.getValue();
                        V v1 = e1.getValue();
                        return v0.compareTo(v1);
                    }
                };
        PriorityQueue<Map.Entry<K, V>> highest =
                new PriorityQueue<Map.Entry<K, V>>(n, comparator);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            highest.offer(entry);
            while (highest.size() > n) {
                highest.poll();
            }
        }

        List<Map.Entry<K, V>> result = new ArrayList<Map.Entry<K, V>>();
        while (highest.size() > 0) {
            result.add(highest.poll());
        }
        return result;
    }

    public static List<Block> getPercantage(List<Block> blocks, int percentage) {
        List<Block> randomBlocks = new ArrayList<>();
        double blockAmount = Math.round(blocks.size() * ((double) percentage / 100));
        Collections.shuffle(blocks);
        for (int i = 0; i < blockAmount; i++) {
            randomBlocks.add(blocks.get(i));
        }
        return randomBlocks;
    }
}

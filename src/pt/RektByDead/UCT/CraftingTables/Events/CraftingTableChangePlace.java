package pt.RektByDead.UCT.CraftingTables.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.List;

public class CraftingTableChangePlace implements Listener {
    @EventHandler
    public void onPistonExtends(BlockPistonExtendEvent e) {
        List<Block> blockList = e.getBlocks();
        for (Block block : blockList) {
            if (block.getType() == Material.CRAFTING_TABLE) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent e) {
        List<Block> blockList = e.getBlocks();
        for (Block block : blockList) {
            if (block.getType() == Material.CRAFTING_TABLE) {
                e.setCancelled(true);
                return;
            }
        }
    }
}

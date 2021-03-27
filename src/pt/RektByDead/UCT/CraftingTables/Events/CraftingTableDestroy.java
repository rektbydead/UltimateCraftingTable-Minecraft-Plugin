package pt.RektByDead.UCT.CraftingTables.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;


public class CraftingTableDestroy implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled())
            return;

        Block block = e.getBlock();
        if (block.getType() != Material.CRAFTING_TABLE)
            return;

        CraftingTable ct = Main.manager.getCrafingTable(block.getLocation());
        if (ct == null)
            return;

        for (ItemStack item : ct.getAllItems())
            if (item != null && item.getType() != Material.AIR)
                ct.getLocation().getWorld().dropItemNaturally(ct.getLocation(), item);

        Main.manager.removeCraftingTable(block.getLocation());
    }

    @EventHandler
    public void onCTExplosion(BlockExplodeEvent e) {
        Block block = e.getBlock();

        if (block.getType() != Material.CRAFTING_TABLE)
            return;

        Main.manager.removeCraftingTable(block.getLocation());
    }
}

package pt.RektByDead.UCT.CraftingTables.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;

public class CraftingTableCreate implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.isCancelled())
            return;

        Block block = e.getBlock();
        if (block.getType() != Material.CRAFTING_TABLE)
            return;

        Main.manager.createCraftingTable(block.getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.isCancelled())
            return;

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = e.getClickedBlock();
        if (block.getType() != Material.CRAFTING_TABLE)
            return;

        CraftingTable ct = Main.manager.getCrafingTable(block.getLocation());

        if (ct != null) {
            ct.setLastTimeOpen(System.currentTimeMillis());
            return;
        }

        Main.manager.createCraftingTable(block.getLocation());
    }
}

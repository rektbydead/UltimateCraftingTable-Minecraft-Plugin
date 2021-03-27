package pt.RektByDead.UCT.Runnables;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;

import java.util.HashMap;
import java.util.Map;

public class DeleteCraftingTables extends BukkitRunnable {

    @Override
    public void run() {
        HashMap<Location, CraftingTable> cts = Main.manager.getAllCrafingTables();

        for (Map.Entry<Location, CraftingTable> entry : cts.entrySet()) {
            CraftingTable ct = entry.getValue();
            if (System.currentTimeMillis() > ct.getLastTimeOpen() + Main.config.getHoursToDeleteCT() * 1000)
                Main.manager.removeCraftingTable(ct.getLocation());
        }
    }
}

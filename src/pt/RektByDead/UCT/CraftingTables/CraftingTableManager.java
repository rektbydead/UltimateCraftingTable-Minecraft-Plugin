package pt.RektByDead.UCT.CraftingTables;

import org.bukkit.Location;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;

import java.util.HashMap;


public class CraftingTableManager {
    private HashMap<Location, CraftingTable> craftingTables;

    public CraftingTableManager() {
        craftingTables = new HashMap<>();
    }

    public CraftingTable getCrafingTable(Location loc) {
        return craftingTables.get(loc);
    }

    public boolean removeCraftingTable(Location loc) {
        return craftingTables.remove(loc) != null;
    }

    public CraftingTable createCraftingTable(Location loc) {
        CraftingTable ct = new CraftingTable(loc);
        craftingTables.put(loc, ct);
        return ct;
    }

    public boolean isCraftingTable(Location loc) {
        return craftingTables.containsKey(loc);
    }

    public void changeLocation(Location oldLoc, Location newLoc) {
        CraftingTable ct = craftingTables.get(oldLoc);
        if (ct == null) {
            craftingTables.put(newLoc, new CraftingTable(newLoc));
            return;
        }

        ct.setNewLocation(newLoc);
        craftingTables.remove(oldLoc);
        craftingTables.put(newLoc, ct);
    }

    public HashMap<Location, CraftingTable> getAllCrafingTables() {
        return craftingTables;
    }
}

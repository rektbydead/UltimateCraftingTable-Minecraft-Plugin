package pt.RektByDead.UCT.CraftingTables.Model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingTable {
    //private static int idCounter = 0;
    //private int id;

    private final static int InventorySize = 9;
    private Location loc;
    private ItemStack[] slots;
    private List<Player> PlayersUsingTable;

    private long lastTimeOpen;

    public CraftingTable(Location loc) {
        //this.id = idCounter++;
        this.loc = loc.clone();
        slots = new ItemStack[InventorySize];
        PlayersUsingTable = new ArrayList<>();
        for (int i = 0; i < InventorySize; i++)
            slots[i] = new ItemStack(Material.AIR);

        lastTimeOpen = System.currentTimeMillis();
    }

    //public int getID() { return id; }

    public long getLastTimeOpen() {
        return lastTimeOpen;
    }

    public void setLastTimeOpen(long now) {
        lastTimeOpen = now;
    }

    public void setNewLocation(Location newLoc) {
        this.loc = newLoc.clone();
    }

    public Location getLocation() {
        return loc;
    }

    public void addPlayer(Player p) {
        PlayersUsingTable.add(p);
    }

    public boolean removePlayer(Player p) {
        return PlayersUsingTable.remove(p);
    }

    public void changePlayersInventory() {
        for (Player p : PlayersUsingTable) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            for (int i = 0; i < InventorySize; i++)
                inv.setItem(i + 1, slots[i]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof CraftingTable))
            return false;

        CraftingTable that = (CraftingTable) o;
        return loc.equals(that.loc);
    }

    public final ItemStack getItemAt(int slot) {
        return (slot >= 0 && slot < InventorySize) ? slots[slot] : null;
    }

    public void setItemAt(int slot, ItemStack item) {
        slots[slot] = item == null ? null : item.clone();
    }

    public ItemStack[] getAllItems() {
        return slots;
    }

    public boolean removeItemAt(int slot) {
        if (slot >= 0 && slot < InventorySize)
            return false;

        if (slots[slot] == null)
            return false;

        slots[slot] = null;
        return true;
    }

    public void removeAllItems() {
        for (int i = 0; i < InventorySize; i++)
            slots[i] = null;
    }
}

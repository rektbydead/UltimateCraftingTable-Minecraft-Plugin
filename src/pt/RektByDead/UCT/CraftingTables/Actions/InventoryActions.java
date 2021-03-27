package pt.RektByDead.UCT.CraftingTables.Actions;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;

public class InventoryActions {

    public static void actionCollect_To_Cursor(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        Inventory inv = e.getView().getTopInventory();
        ItemStack itemToCollect = e.getCursor();
        ItemStack item;

        int amount = itemToCollect.getAmount();
        for (int i = 0; i < 9; i++) {
            if (amount >= itemToCollect.getMaxStackSize())
                break;

            item = inv.getItem(i + 1);
            if (item == null)
                continue;

            if (item.getType() != itemToCollect.getType())
                continue;

            amount += item.getAmount();
            if (amount > itemToCollect.getMaxStackSize()) {
                int retirar = amount - itemToCollect.getMaxStackSize();
                item.setAmount(retirar);
                amount = itemToCollect.getMaxStackSize();
                ct.setItemAt(i, item.getAmount() == 0 ? null : item);
            } else
                ct.setItemAt(i, null);
        }

        itemToCollect.setAmount(amount);
        e.setCursor(itemToCollect);
        ct.changePlayersInventory();
    }

    public static void actionHotBar_Swap(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        Inventory inv = e.getWhoClicked().getInventory();
        ItemStack itemInv = e.getInventory().getItem(e.getSlot());
        ItemStack itemHotBar = inv.getItem(e.getHotbarButton());

        inv.setItem(e.getHotbarButton(), itemInv);

        ct.setItemAt(e.getSlot() - 1, itemHotBar);
        ct.changePlayersInventory();
    }

    public static void actionDropOneSlot(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        Inventory inv = e.getInventory();
        ItemStack item = inv.getItem(e.getSlot());

        ItemStack dropItem = item.clone();
        dropItem.setAmount(1);
        inv.getLocation().getWorld().dropItemNaturally(inv.getLocation(), dropItem);

        item.setAmount(item.getAmount() - 1);
        ct.setItemAt(e.getSlot() - 1,/*item.getAmount() == 0 ? null :*/ item);
        ct.changePlayersInventory();
    }

    public static void actionDropAllSlot(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        Inventory inv = e.getInventory();
        ItemStack item = inv.getItem(e.getSlot());

        inv.getLocation().getWorld().dropItemNaturally(inv.getLocation(), item);

        ct.setItemAt(e.getSlot() - 1, null);
        ct.changePlayersInventory();
    }

    public static void actionPlaceOne(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        ItemStack cursor = e.getCursor();
        ItemStack itemInventory = cursor.clone();

        cursor.setAmount(cursor.getAmount() - 1);
        e.setCursor(cursor);

        Inventory inv = e.getClickedInventory();
        ItemStack replaced = inv.getItem(e.getSlot());

        if (replaced == null)
            itemInventory.setAmount(1);
        else itemInventory.setAmount(replaced.getAmount() + 1);

        ct.setItemAt(e.getSlot() - 1, itemInventory);
        ct.changePlayersInventory();
    }

    public static void actionPlaceSome(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        ItemStack cursor = e.getCursor();//.clone();
        ItemStack replaced = e.getInventory().getItem(e.getSlot());

        int sobra = (replaced.getAmount() + cursor.getAmount()) - replaced.getMaxStackSize();
        cursor.setAmount(sobra);
        e.setCursor(cursor);
        replaced.setAmount(replaced.getMaxStackSize());

        ct.setItemAt(e.getSlot() - 1, replaced);
        ct.changePlayersInventory();
    }

    public static void actionPlaceAll(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        ItemStack cursor = e.getCursor();
        e.setCursor(null);

        ItemStack invItem = e.getInventory().getItem(e.getSlot());

        if (invItem != null)
            cursor.setAmount(cursor.getAmount() + invItem.getAmount());

        ct.setItemAt(e.getSlot() - 1, cursor);
        ct.changePlayersInventory();
    }

    public static void actionPickUpHalf(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        Inventory inv = e.getClickedInventory();
        ItemStack itemInventory = inv.getItem(e.getSlot());

        if (itemInventory.getAmount() == 1) {
            actionPickUpAll(e);
            return;
        }

        int half = itemInventory.getAmount() / 2;
        ItemStack itemInventoryClone = itemInventory.clone();
        itemInventoryClone.setAmount(half);

        itemInventory.setAmount(itemInventory.getAmount() - half);

        e.setCursor(itemInventoryClone);

        ct.setItemAt(e.getSlot() - 1, itemInventory);
        ct.changePlayersInventory();
    }

    public static void actionPickUpAll(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        Inventory inv = e.getClickedInventory();
        ItemStack itemInventory = inv.getItem(e.getSlot());
        e.setCursor(itemInventory);

        ct.setItemAt(e.getSlot() - 1, null);
        ct.changePlayersInventory();
    }

    public static void craftItemMoveHotBar(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();

        ItemStack getItem = inv.getItem(e.getSlot());
        if (getItem == null) //if theres no item to craft, exit
            return;

        ItemStack itemHotBar = e.getWhoClicked().getInventory().getItem(e.getHotbarButton());
        if (itemHotBar == null)
            itemHotBar = new ItemStack(Material.AIR, 0);
        else if (itemHotBar.getType() != getItem.getType())
            return;

        if (itemHotBar.getAmount() + getItem.getAmount() > getItem.getMaxStackSize())
            return;

        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        for (int i = 0; i < 9; i++) {
            ItemStack item = inv.getItem(i + 1);
            if (item == null)
                continue;

            item.setAmount(item.getAmount() - 1);
            ct.setItemAt(i, item);
        }

        getItem.setAmount(itemHotBar.getAmount() + getItem.getAmount());
        e.getWhoClicked().getInventory().setItem(e.getHotbarButton(), getItem);
        ct.changePlayersInventory();
    }

    public static void craftItemClick(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();

        ItemStack getItem = inv.getItem(e.getSlot());
        if (getItem == null)
            return;

        ItemStack itemCursor = e.getCursor();

        if (itemCursor.getType() != Material.AIR && itemCursor.getType() != getItem.getType())
            return;

        if (itemCursor.getAmount() + getItem.getAmount() > getItem.getMaxStackSize())
            return;

        int quantidade = itemCursor.getAmount() + getItem.getAmount();
        if (getItem.getMaxStackSize() < quantidade)
            return;

        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        for (int i = 0; i < 9; i++) {
            ItemStack item = inv.getItem(i + 1);
            if (item == null)
                continue;

            item.setAmount(item.getAmount() - 1);
            ct.setItemAt(i, item);
        }

        getItem.setAmount(quantidade);
        e.setCursor(getItem);

        ct.changePlayersInventory();
    }

    public static void craftItemShift(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();

        ItemStack getItem = inv.getItem(e.getSlot());
        if (getItem == null) //if theres no item to craft, exit
            return;

        Inventory playerInv = e.getWhoClicked().getOpenInventory().getBottomInventory();

        int itemsCraftable = -1;
        for (int i = 0; i < 9; i++) { //goes through all items in crafting table
            ItemStack item = inv.getItem(i + 1); //and checks how many items can be crafted
            if (item != null)
                if (itemsCraftable == -1 || itemsCraftable > item.getAmount())
                    itemsCraftable = item.getAmount();
        }

        int availableSpace = 0;
        for (int i = 0; i < 36; i++)
            if (playerInv.getItem(i) == null)
                availableSpace += getItem.getMaxStackSize();
            else if (playerInv.getItem(i).getType() == getItem.getType())
                availableSpace += getItem.getMaxStackSize() - playerInv.getItem(i).getAmount();

        if (availableSpace < itemsCraftable * getItem.getAmount()) //if theres more craftable items than space
            itemsCraftable = availableSpace / getItem.getAmount(); //changes the number of craftable items

        if (itemsCraftable == 0)
            return;

        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        for (int i = 0; i < 9; i++) { //goes through all inventory and removes the number of items
            ItemStack item = inv.getItem(i + 1);
            if (item == null)
                continue;

            item.setAmount(item.getAmount() - itemsCraftable);
            ct.setItemAt(i, item);
        }

        ct.changePlayersInventory();
        getItem.setAmount(itemsCraftable * getItem.getAmount());

        if (getItem.getMaxStackSize() < getItem.getAmount()) {
            int divisor = getItem.getAmount() / getItem.getMaxStackSize();
            int resto = getItem.getAmount() % getItem.getMaxStackSize();
            getItem.setAmount(getItem.getMaxStackSize());
            for (int i = 0; i < divisor; i++)
                playerInv.addItem(getItem);
            getItem.setAmount(resto);
            getItem.setAmount(getItem.getMaxStackSize());
        }

        playerInv.addItem(getItem);
    }

    public static void actionMove_To_Other_Inventory(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        ItemStack getItem = inv.getItem(e.getSlot());
        if (getItem == null) //if theres no item to craft, exit
            return;

        Inventory playerInv = e.getWhoClicked().getOpenInventory().getBottomInventory();

        int availableSpace = 0;
        for (int i = 0; i < 36; i++)
            if (playerInv.getItem(i) == null)
                availableSpace += getItem.getMaxStackSize();
            else if (playerInv.getItem(i).getType() == getItem.getType())
                availableSpace += getItem.getMaxStackSize() - playerInv.getItem(i).getAmount();

        if (availableSpace < getItem.getAmount()) //if theres more craftable items than space
            return;


        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        ct.setItemAt(e.getSlot() - 1, null);
        ct.changePlayersInventory();

        playerInv.addItem(getItem);
    }

    public static void actionSwap_With_Cursor(InventoryClickEvent e) {
        CraftingTable ct = Main.manager.getCrafingTable(e.getInventory().getLocation());
        if (ct == null)
            return;

        ItemStack cursor = e.getCursor();
        ItemStack itemInv = e.getInventory().getItem(e.getSlot());

        e.setCursor(itemInv);

        ct.setItemAt(e.getSlot() - 1, cursor);
        ct.changePlayersInventory();
    }
}

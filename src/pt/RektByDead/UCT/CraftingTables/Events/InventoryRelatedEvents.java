package pt.RektByDead.UCT.CraftingTables.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pt.RektByDead.UCT.CraftingTables.Actions.InventoryActions;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;

import java.util.Map;

public class InventoryRelatedEvents implements Listener {

    @EventHandler
    public void onDragItemInventory(InventoryDragEvent e) {
        Inventory inv = e.getInventory();
        //Bukkit.broadcastMessage(e.getType() + "");
        if (inv.getType() != InventoryType.WORKBENCH) //this is here so it doesnt have to do all those ifs bellow if it isnt a workbench
            return;

        CraftingTable ct = Main.manager.getCrafingTable(inv.getLocation());
        if (ct == null)
            return;

        Map<Integer, ItemStack> newItems = e.getNewItems();
        ItemStack cursor = e.getOldCursor();
        for (Map.Entry<Integer, ItemStack> itemEntry : newItems.entrySet()) {
            ItemStack item = itemEntry.getValue();
            if (item != null && item.getType() != cursor.getType())
                continue;

            cursor.setAmount(cursor.getAmount() - item.getAmount());

            if (itemEntry.getKey() < 1 || itemEntry.getKey() > 10)
                continue;

            ct.setItemAt(itemEntry.getKey() - 1, item);
        }

        e.setCursor(cursor);
        ct.changePlayersInventory();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void cancelShiftIFWorkBenchOpen(InventoryClickEvent e) {
        if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY)
            return;

        Inventory top = e.getWhoClicked().getOpenInventory().getTopInventory();
        Inventory clicked = e.getClickedInventory();

        if (clicked == null)
            return;

        if (top.getType() != InventoryType.WORKBENCH)
            return;

        if (!(clicked instanceof PlayerInventory))
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.isCancelled())
            return;

        Inventory inv = e.getClickedInventory();

        if (inv == null || inv.getType() != InventoryType.WORKBENCH) //this is here so it doesnt have to do all those ifs bellow if it isnt a workbench
            return;

        InventoryAction action = e.getAction();
        if (action == InventoryAction.NOTHING)
            return;

        e.setCancelled(true);

        if (e.getSlot() == 0) {
            if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY)
                InventoryActions.craftItemShift(e);
            else if (action == InventoryAction.HOTBAR_MOVE_AND_READD || action == InventoryAction.HOTBAR_SWAP)
                InventoryActions.craftItemMoveHotBar(e);
            else if (action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_HALF || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME)
                InventoryActions.craftItemClick(e);
            return;
        }

        if (action == InventoryAction.PLACE_ONE)
            InventoryActions.actionPlaceOne(e);
        else if (action == InventoryAction.PLACE_ALL)
            InventoryActions.actionPlaceAll(e);
        else if (action == InventoryAction.PLACE_SOME)
            InventoryActions.actionPlaceSome(e);
        else if (action == InventoryAction.PICKUP_HALF)
            InventoryActions.actionPickUpHalf(e);
        else if (action == InventoryAction.PICKUP_ALL)
            InventoryActions.actionPickUpAll(e);
        else if (action == InventoryAction.DROP_ONE_SLOT)
            InventoryActions.actionDropOneSlot(e);
        else if (action == InventoryAction.DROP_ALL_SLOT)
            InventoryActions.actionDropAllSlot(e);
        else if (action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.HOTBAR_MOVE_AND_READD)
            InventoryActions.actionHotBar_Swap(e);
        else if (action == InventoryAction.SWAP_WITH_CURSOR)
            InventoryActions.actionSwap_With_Cursor(e);
        else if (action == InventoryAction.COLLECT_TO_CURSOR)
            InventoryActions.actionCollect_To_Cursor(e);
        else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY)
            InventoryActions.actionMove_To_Other_Inventory(e);
    }
}

package pt.RektByDead.UCT.CraftingTables.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;

public class CraftingTableOpenClose implements Listener {
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getType() != InventoryType.WORKBENCH)
            return;

        CraftingTable ct = Main.manager.getCrafingTable(inv.getLocation());
        if (ct == null)
            return;

        if (!ct.getIsOpen()) {
            if (!Main.config.getUsableByMultiplePlayers()) {
                e.setCancelled(true);
                String msg = Main.config.getMessage("MessageIfCrafingTableAlreadyOpen");
                if (!msg.equals("-NAO-MANDAR-MENSAGEM-"))
                    e.getPlayer().sendMessage(Main.config.getMessage("MessageIfCrafingTableAlreadyOpen"));
                return;
            }
        }

        ct.addPlayer((Player) e.getPlayer());
        ct.setIsOpen(false);

        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            for (int i = 0; i < 9; i++)
                inv.setItem(i + 1, ct.getItemAt(i));
        }, 1);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getType() != InventoryType.WORKBENCH)
            return;

        for (int i = 0; i < 10; i++)
            inv.setItem(i, null);

        CraftingTable ct = Main.manager.getCrafingTable(inv.getLocation());
        if (ct == null)
            return;

        ct.setIsOpen(true);
        ct.removePlayer((Player) e.getPlayer());
    }
}

package pt.RektByDead.UCT.Runnables;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveItemsAsync extends BukkitRunnable {

    @Override
    public void run() {
        try {
            File file = new File("plugins/" + Main.instance.getName() + "/savedCraftingTables.yml");
            if (!file.exists())
                if (!file.createNewFile())
                    return;

            YamlConfiguration loading = YamlConfiguration.loadConfiguration(file);

            final HashMap<Location, CraftingTable> craftingTables = (HashMap<Location, CraftingTable>) Main.manager.getAllCrafingTables().clone();
            int i = 0;
            for (Map.Entry<Location, CraftingTable> entry : craftingTables.entrySet()) {
                CraftingTable cp = entry.getValue();
                loading.set("craftingTables." +i + ".location", cp.getLocation());
                loading.set("craftingTables." + i + ".items",cp.getAllItems());
                loading.set("craftingTables." + i + ".lastTimeSeen", String.valueOf(cp.getLastTimeOpen()));
                i++;
            }

            loading.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package pt.RektByDead.UCT;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pt.RektByDead.UCT.CraftingTables.CraftingTableManager;
import pt.RektByDead.UCT.CraftingTables.Model.CraftingTable;
import pt.RektByDead.UCT.Events.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {
    public static CraftingTableManager manager;
    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        manager = new CraftingTableManager();
        enableAllEvents();

        File folder = new File("plugins/" + instance.getName());
        if (!folder.exists())
            saveDefaultConfig();
        loadCraftingTables();
        loadConfig();

        asyncItemsSaving();
    }

    private void asyncItemsSaving() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, this::saveCraftingTables, 300 * 2 * 20L, 300 * 2 * 20L);
    }

    private void enableAllEvents(){
        getServer().getPluginManager().registerEvents(new CraftingTableDestroy(), instance);
        getServer().getPluginManager().registerEvents(new CraftingTableCreate(), instance);
        getServer().getPluginManager().registerEvents(new CraftingTableOpenClose(), instance);
        getServer().getPluginManager().registerEvents(new InventoryRelatedEvents(), instance);
        getServer().getPluginManager().registerEvents(new CraftingTableChangePlace(), instance);
    }

    public void onDisable() {
        saveCraftingTables();
    }

    private void loadCraftingTables() {
        try {
            File file = new File("plugins/" + instance.getName() + "/savedCraftingTables.yml");
            if (!file.exists())
                if (!file.createNewFile())
                    return;

            YamlConfiguration loading = YamlConfiguration.loadConfiguration(file);

            ConfigurationSection section = loading.getConfigurationSection("craftingTables");
            if (section == null)
                return;

            for (String path : section.getKeys(false)) {
                ConfigurationSection subSection = loading.getConfigurationSection(section.getCurrentPath() + "." + path);
                if (subSection == null)
                    return;

                String longtime = (String) subSection.get("lastTimeSeen");
                if (longtime == null)
                    continue;

                long lastTime = Long.parseLong(longtime);
                long now = System.currentTimeMillis();
                if (now > lastTime + (1000 * 60 * 60 * 24 * 7))  //has been a week
                    continue;

                Location loc = (Location) subSection.get("location");
                if (loc == null)
                    continue;

                if (loc.getBlock().getType() != Material.CRAFTING_TABLE)
                    continue;

                CraftingTable ct = manager.createCraftingTable(loc);

                List<ItemStack> matrix = (List<ItemStack>) subSection.get("items");

                if (matrix == null)
                    return;

                for (int i = 0; i < matrix.size(); i++)
                    ct.setItemAt(i, matrix.get(i));

                ct.setLastTimeOpen(lastTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        //to do | create
    }

    private void saveCraftingTables() {
        try {
            File file = new File("plugins/" + instance.getName() + "/savedCraftingTables.yml");
            if (!file.exists())
                if (!file.createNewFile())
                    return;

            YamlConfiguration loading = YamlConfiguration.loadConfiguration(file);

            final HashMap<Location, CraftingTable> craftingTables = manager.getAllCrafingTables();
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

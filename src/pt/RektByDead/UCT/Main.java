package pt.RektByDead.UCT;


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
                //System.out.println("\n\n\n"+section.getCurrentPath() + "." + path);

                ConfigurationSection subSection = loading.getConfigurationSection(section.getCurrentPath() + "." + path);
                if (subSection == null)
                    return;
                //System.out.println("Chegou 1");

                String longtime = (String) subSection.get("lastTimeSeen");
                if (longtime == null)
                    continue;

                //System.out.println("Chegou 2");
                long lastTime = Long.parseLong(longtime);
                long now = System.currentTimeMillis();
                //System.out.println(now + " < " + (lastTime + (1000 * 60 * 60 * 24 * 7)));
                if (now > lastTime + (1000 * 60 * 60 * 24 * 7))  //has been a week
                    continue;
                //System.out.println("Chegou 3");

                Location loc = (Location) subSection.get("location");
                if (loc == null)
                    continue;
                //System.out.println("Chegou 4");
                if (loc.getBlock().getType() != Material.CRAFTING_TABLE)
                    continue;
                //System.out.println("Chegou 5");
                CraftingTable ct = manager.createCraftingTable(loc);

                List<ItemStack> matrix = (List<ItemStack>) subSection.get("items");
                //for (ItemStack a : matrix)
                //    System.out.println(a + "");

                if (matrix == null)
                    return;
                //System.out.println("Chegou 6");
                for (int i = 0; i < matrix.size(); i++)
                    ct.setItemAt(i, matrix.get(i));

                ct.setLastTimeOpen(lastTime);
                //System.out.println("Chegou 7");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {

    }

    private void saveCraftingTables() {
        try {
            File file = new File("plugins/" + instance.getName() + "/savedCraftingTables.yml");
            if (!file.exists())
                if (!file.createNewFile())
                    return;

            YamlConfiguration loading = YamlConfiguration.loadConfiguration(file);

            HashMap<Location, CraftingTable> craftingTables = manager.getAllCrafingTables();
            int i = 0;
            for (Map.Entry<Location, CraftingTable> ct : craftingTables.entrySet()) {
                loading.set("craftingTables." + i + ".location", ct.getKey());
                loading.set("craftingTables." + i + ".items", ct.getValue().getAllItems());
                loading.set("craftingTables." + i + ".lastTimeSeen", String.valueOf(ct.getValue().getLastTimeOpen()));
                i++;
            }

            loading.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

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
import pt.RektByDead.UCT.CraftingTables.Events.*;
import pt.RektByDead.UCT.Runnables.DeleteCraftingTables;
import pt.RektByDead.UCT.Runnables.SaveItemsAsync;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    public static CraftingTableManager manager;
    public static Plugin instance;
    public static Config config;

    @Override
    public void onEnable() {
        instance = this;
        manager = new CraftingTableManager();
        config = new Config(getConfig());
        enableAllEvents();

        File folder = new File("plugins/" + instance.getName());
        if (!folder.exists())
            saveDefaultConfig();
        loadCraftingTables();
        loadConfig();

        new SaveItemsAsync().runTaskTimerAsynchronously(instance, Main.config.getSaveItemsEveryXSeconds(), Main.config.getSaveItemsEveryXSeconds());

        if (Main.config.getHoursToCheckDeleteCT() > 0)
            new DeleteCraftingTables().runTaskTimerAsynchronously(instance, Main.config.getHoursToCheckDeleteCT(), Main.config.getHoursToCheckDeleteCT());
    }

    private void enableAllEvents(){
        getServer().getPluginManager().registerEvents(new CraftingTableDestroy(), instance);
        getServer().getPluginManager().registerEvents(new CraftingTableCreate(), instance);
        getServer().getPluginManager().registerEvents(new CraftingTableOpenClose(), instance);
        getServer().getPluginManager().registerEvents(new InventoryRelatedEvents(), instance);
        getServer().getPluginManager().registerEvents(new CraftingTableChangePlace(), instance);
    }

    public void onDisable() {
        new SaveItemsAsync().run();
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
                if (now > lastTime + Main.config.getHoursToDeleteCT() * 1000)
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
}

package pt.RektByDead.UCT;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class Config {
    private int saveItemsEveryXSeconds;
    private boolean usableByMultiplePlayers;
    private int hoursToDeleteCT;
    private int hoursToCheckDeleteCraftingTable;


    private HashMap<String, String> mensagens;

    public Config(FileConfiguration config) {
        saveItemsEveryXSeconds = (int) config.get("saveItemsEveryXSeconds");
        usableByMultiplePlayers = (boolean) config.get("usableByMultiplePlayers");
        hoursToDeleteCT = (int) config.get("hoursToDeleteCraftingTable");
        hoursToCheckDeleteCraftingTable = (int) config.get("hoursToCheckDeleteCraftingTable");

        mensagens = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("mensagens");
        if (section == null)
            return;

        for (String path : section.getKeys(false))
            mensagens.put(path, (String) section.get(path));
    }


    public String getMessage(String message) { return mensagens.get(message); }

    public int getSaveItemsEveryXSeconds() { return saveItemsEveryXSeconds; }
    public boolean getUsableByMultiplePlayers() { return usableByMultiplePlayers; }

    public int getHoursToDeleteCT() { return hoursToDeleteCT; }
    public int getHoursToCheckDeleteCT() { return hoursToCheckDeleteCraftingTable; }
}



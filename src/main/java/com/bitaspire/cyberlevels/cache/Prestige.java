package com.bitaspire.cyberlevels.cache;

import com.bitaspire.cyberlevels.CyberLevels;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.util.TreeMap;

public class Prestige {
    public TreeMap<Integer, Integer> levelsByMinRebirth = new TreeMap<>();

    public Prestige(CyberLevels main) {
        try {
            CLVFile file = new CLVFile(main, "prestiges");

            ConfigurationSection section = file.getSection("prestiges");
            if (section != null) {
                section.getKeys(false).forEach(key -> levelsByMinRebirth.put(Integer.parseInt(key), section.getInt(key)));
            }
        }
        catch (IOException ignored) {}
    }

}

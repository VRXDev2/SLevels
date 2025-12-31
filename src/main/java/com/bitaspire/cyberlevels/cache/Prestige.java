package com.bitaspire.cyberlevels.cache;

import com.bitaspire.cyberlevels.CyberLevels;

import java.io.IOException;

public class Prestige {
    public int levelsPerPrestige = 500;

    public Prestige(CyberLevels main) {
        try {
            CLVFile file = new CLVFile(main, "prestiges");
            levelsPerPrestige = Integer.parseInt(file.get("prestiges.levelsPerPrestige", 500) + "");
        }
        catch (IOException ignored) {}
    }

}

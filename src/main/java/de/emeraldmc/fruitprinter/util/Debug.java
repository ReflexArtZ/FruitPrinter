package de.emeraldmc.fruitprinter.util;

import de.emeraldmc.fruitprinter.FruitPrinter;
import org.bukkit.Bukkit;

public class Debug {
    public static void print(String s) {
        if (FruitPrinter.configuration.isDebug()) {
            Bukkit.getConsoleSender().sendMessage(ChatAPI.translateColor(FruitPrinter.configuration.getPrefix())+"§c§lDEBUG §r> "+s);
        }
    }
}

package de.emeraldmc.fruitprinter;

import de.Herbystar.TTA.TTA_Methods;
import de.emeraldmc.fruitprinter.commands.GiveCmd;
import de.emeraldmc.fruitprinter.io.OwnConfiguration;
import de.emeraldmc.fruitprinter.io.StandardConfiguration;
import de.emeraldmc.fruitprinter.io.sqlite.PrinterDB;
import de.emeraldmc.fruitprinter.listener.PrinterBreakEvent;
import de.emeraldmc.fruitprinter.listener.PrinterDropEvent;
import de.emeraldmc.fruitprinter.listener.PrinterInteractEvent;
import de.emeraldmc.fruitprinter.listener.PrinterPlaceEvent;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.objects.PrinterType;
import de.emeraldmc.fruitprinter.util.Debug;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

public final class FruitPrinter extends JavaPlugin {

    private static FruitPrinter instance;

    public static StandardConfiguration configuration;
    public static OwnConfiguration levelConfiguration;

    public static PrinterDB printerDB;


    private Economy economy;

    public static FruitPrinter getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;

    }

    @Override
    public void onEnable() {
        initializeStandartConfig();
        configuration = new StandardConfiguration();
        Debug.print("Initialized Standard Configuration");
        levelConfiguration = new OwnConfiguration("levels.yml");
        Debug.print("Initialized Level Configuration");

        try {
            printerDB = new PrinterDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Debug.print("Initialized Level Configuration");

        setupEconomy();
        Debug.print("Set up Economy");

        registerCommands();
        Debug.print("Registered Commands");
        registerEvents();
        Debug.print("Registered Events");

        PrinterType.loadPrinters();
        Material.
    }

    @Override
    public void onDisable() {
        TTA_Methods.setHoloPlayers(null);
    }

    /**
     * Loading standart config
     */
    private void initializeStandartConfig() {
        File f = new File(getDataFolder()+ File.separator+ "config.yml");
        if(!f.exists())
        {
            this.saveDefaultConfig();
        }
        this.reloadConfig();
    }
    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private void registerCommands() {
        getCommand("mp").setExecutor(new GiveCmd());
    }
    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PrinterPlaceEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PrinterBreakEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PrinterInteractEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PrinterDropEvent(), this);
    }

    public Economy getEconomy() {
        return economy;
    }
}

package de.emeraldmc.fruitprinter.objects;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.util.ChatAPI;
import de.emeraldmc.fruitprinter.util.Debug;
import de.emeraldmc.fruitprinter.util.UUIDItemTagType;
import de.emeraldmc.fruitprinter.util.Utils;
import de.tr7zw.itemnbtapi.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PrinterType {

    private static HashMap<String, PrinterType> printerTypeMap;

    private final String id;
    private final String name;
    private final String skin;
    private final int moneyPerPeriod;
    private final long refreshRate;     // in ms
    private final long destroyTime;     // in ms

    public PrinterType(String id, String name, String skin, int moneyPerPeriod, long refreshRate, long destroyTime) {
        this.id = id;
        this.name = name;
        this.skin = skin;
        this.moneyPerPeriod = moneyPerPeriod;
        this.refreshRate = refreshRate;
        this.destroyTime = destroyTime;
    }

    public static void loadPrinters() {
        printerTypeMap = new HashMap<>();
        for (String key : FruitPrinter.levelConfiguration.getConfigConfiguration().getConfigurationSection("Levels").getKeys(false)) {
            String name = FruitPrinter.levelConfiguration.getConfigConfiguration().getString("Levels."+key+".name");
            String skin = FruitPrinter.levelConfiguration.getConfigConfiguration().getString("Levels."+key+".skin");
            int money = FruitPrinter.levelConfiguration.getConfigConfiguration().getInt("Levels."+key+".money");
            long refreshRate = FruitPrinter.levelConfiguration.getConfigConfiguration().getInt("Levels."+key+".refreshTime");
            long destroyRate = FruitPrinter.levelConfiguration.getConfigConfiguration().getInt("Levels."+key+".destroyTime");
            PrinterType printerType = new PrinterType(key, name, skin, money, refreshRate, destroyRate);

            printerTypeMap.put(key, printerType);

            System.out.println(printerType);
        }
    }

    public static ItemStack getMoneyPrinterItem(MoneyPrinter printer) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        NBTItem nbti = new NBTItem(itemStack);

        // Setting Display Name
        NBTCompound disp = nbti.getCompound("display");
        disp.setString("Name", ChatAPI.translateColor(printer.getLevel().getName()));

        // Setting Skin
        NBTCompound skull = nbti.addCompound("SkullOwner");
        NBTListCompound texture = skull.addCompound("Properties").getList("textures", NBTType.NBTTagCompound).addCompound();

        String url = Utils.urlToBase64(printer.getLevel().getSkin());
        UUID hashAsId = new UUID(url.hashCode(), url.hashCode());
        texture.setString("Value", url);
        skull.setString("Id", hashAsId.toString());

        nbti.setString("UUID", printer.getUuid().toString());
        System.out.println("UUID-SET:"+printer.getUuid().toString());

        itemStack = nbti.getItem();

        return itemStack;
    }

    public static UUID printerUUID(ItemStack stack) {
        NBTItem nbti = new NBTItem(stack);
        String uuidStr = nbti.getString("UUID");
        if (uuidStr == null) return null;

        return UUID.fromString(uuidStr);
    }

    public static HashMap<String, PrinterType> getPrinterTypeMap() {
        return printerTypeMap;
    }

    public String getName() {
        return name;
    }

    public String getSkin() {
        return skin;
    }

    public int getMoneyPerPeriod() {
        return moneyPerPeriod;
    }

    public long getRefreshRate() {
        return refreshRate;
    }

    public long getDestroyTime() {
        return destroyTime;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PrinterType{" +
                "name='" + name + '\'' +
                ", skin='" + skin + '\'' +
                ", moneyPerPeriod=" + moneyPerPeriod +
                ", refreshRate=" + refreshRate +
                ", destroyTime=" + destroyTime +
                '}';
    }
}


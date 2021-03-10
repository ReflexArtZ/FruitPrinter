package de.emeraldmc.fruitprinter.objects;

import de.Herbystar.TTA.TTA_Methods;
import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.util.ChatAPI;
import de.emeraldmc.fruitprinter.util.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;

import org.bukkit.entity.Player;

import ü.TTA_HoloAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MoneyPrinter {
    private static final double MAX_HEALTH = 120;

    private UUID uuid;
    private PrinterType level; //ToDo: Wie printer nach restart wieder neu laden?
    private UUID owner;

    private Location location;

    private double health;   // health of printer
    private int storedMoney;     // money that was generated since last payout
    private long timeUntilDestroy;  // in ms

    private boolean isBurning;
    private ArmorStand burnEntity;


    public MoneyPrinter(UUID uuid, PrinterType level, UUID owner, Location location) {
        this.uuid = uuid;
        this.level = level;
        this.owner = owner;
        this.location = location;
        this.health = MAX_HEALTH;
        storedMoney = 0;
        timeUntilDestroy = level.getDestroyTime();
    }

    public void addStoredMoney(int money) {
        storedMoney += money;
    }

    public int removeStoredMoney() {
        int money = storedMoney;
        storedMoney = 0;
        return money;
    }
    public void removeTimeUntilDestroy(long time) {
        timeUntilDestroy -= time;
    }

    public void addHolo() {
        if (location == null) return;
        TTA_Methods.createHolo(location, holoLines());
        TTA_Methods.setHoloPlayers((Player) Bukkit.getOnlinePlayers());
    }

    public void removeHolo() {
        TTA_HoloAPI holo = holos.get(location);
        Bukkit.getOnlinePlayers().forEach(holo::destroyHolo);
    }

    public void updateHolo() {
        if (location == null) return;
        removeHolo();
        addHolo();
    }

    private List<String> holoLines() {
        ArrayList<String> lines = new ArrayList<>();

        lines.add(ChatAPI.translateColor(level.getName()+"  &8&l|  &r&6"+calcProgressPercentage()+"%"));
        lines.add(ChatAPI.translateColor("&aStored Money: "+storedMoney+"€"));
        return lines;
    }

    private int calcProgressPercentage() {
        return (int) ((100*timeUntilDestroy)/level.getDestroyTime());
    }

    public static double getMaxHealth() {
        return MAX_HEALTH;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PrinterType getLevel() {
        return level;
    }

    public UUID getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public double getHealth() {
        return health;
    }

    public int getStoredMoney() {
        return storedMoney;
    }

    public long getTimeUntilDestroy() {
        return timeUntilDestroy;
    }

    public boolean isBurning() {
        return isBurning;
    }

    public ArmorStand getBurnEntity() {
        return burnEntity;
    }

    public void toggleBurn() {
        if (location == null) return;
        ParticleEffect.FLAME.send(Bukkit.getOnlinePlayers(), location, 0, 1, 0, 0, 1);
    }

    public void destroy() {
        if (location == null) return;
        FruitPrinter.printerDB.removePrinter(uuid);
        Utils.sendLightning((Player)Bukkit.getOnlinePlayers().toArray()[0], location);

        location.getWorld().createExplosion(location, 4f, false, false);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FruitPrinter.getInstance(), () -> location.getWorld().playEffect(location, Effect.SMOKE, 0), 1, 10);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(FruitPrinter.getInstance(), () -> location.getWorld().playSound(location, Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f), 1, 10);
        location.getWorld().spawnParticle(Particle.FLAME, location, 0, 0, 0, 0);
    }

    public MoneyPrinter setLocation(Location location) {
        this.location = location;
        return this;
    }

    public MoneyPrinter setHealth(double health) {
        this.health = health;
        return this;
    }
}

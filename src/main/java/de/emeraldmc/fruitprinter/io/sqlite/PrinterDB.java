package de.emeraldmc.fruitprinter.io.sqlite;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.objects.PrinterType;
import de.emeraldmc.fruitprinter.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

public class PrinterDB {
    private DBController controller;

    public PrinterDB() throws SQLException {
        controller = new DBController(FruitPrinter.getInstance().getDataFolder()+ File.separator+ "printer.db");
        createStorageTable();
    }

    synchronized private void createStorageTable() {
        String sql = "CREATE TABLE IF NOT EXISTS 'Printers' ('ID' INTEGER PRIMARY KEY , 'PrinterUUID' VARCHAR(36), 'OwnerUUID' VARCHAR(36), 'level' VARCHAR, 'world' VARCHAR(64) NULL , 'x' INTEGER NULL, 'y' INTEGER NULL, 'z' INTEGER NULL, 'storedMoney' DOUBLE NULL, 'timeUntilDestruction' INTEGER NULL );";
        try (Statement stmnt = controller.getStatement()) {
            stmnt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    synchronized public boolean addPrinter(MoneyPrinter printer) {
        if (printer == null) return false;
        try (PreparedStatement stmnt = controller.getPreparedStatement("INSERT INTO Printers (PrinterUUID, OwnerUUID, level, world, x, y, z, storedMoney, timeUntilDestruction) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            stmnt.setString(1, printer.getUuid().toString());
            stmnt.setString(2, printer.getOwner().toString());
            stmnt.setString(3, printer.getLevel().getId());
            if (printer.getLocation() != null) {
                stmnt.setString(4, printer.getLocation().getWorld().getName());
                stmnt.setInt(5, printer.getLocation().getBlockX());
                stmnt.setInt(6, printer.getLocation().getBlockY());
                stmnt.setInt(7, printer.getLocation().getBlockZ());
            } else {
                stmnt.setString(4, null);
                stmnt.setInt(5, -1);
                stmnt.setInt(6, -1);
                stmnt.setInt(7, -1);
            }
            stmnt.setDouble(8, printer.getStoredMoney());
            stmnt.setInt(9, (int) printer.getTimeUntilDestroy());
            stmnt.executeUpdate();
            Debug.print("Added Printer!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    synchronized public boolean updatePrinter(MoneyPrinter printer) {
        if (printer == null) return false;
        try (PreparedStatement stmnt = controller.getPreparedStatement("UPDATE Printers SET level=?, world=?, x=?, y=?, z=?, storedMoney=?, timeUntilDestruction=? WHERE PrinterUUID=?")) {
            stmnt.setString(1, printer.getLevel().getId());
            if (printer.getLocation() != null) {
                stmnt.setString(2, printer.getLocation().getWorld().getName());
                stmnt.setInt(3, printer.getLocation().getBlockX());
                stmnt.setInt(4, printer.getLocation().getBlockY());
                stmnt.setInt(5, printer.getLocation().getBlockZ());
            } else {
                stmnt.setString(2, null);
                stmnt.setInt(3, -1);
                stmnt.setInt(4, -1);
                stmnt.setInt(5, -1);
            }
            stmnt.setDouble(6, printer.getStoredMoney());
            stmnt.setInt(7, (int) printer.getTimeUntilDestroy());
            stmnt.setString(8, printer.getUuid().toString());
            return stmnt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public MoneyPrinter getPrinter(UUID printerUUID) {
        if (printerUUID == null) return null;

        MoneyPrinter printer = null;
        try (PreparedStatement stmnt = controller.getPreparedStatement("SELECT * FROM Printers WHERE PrinterUUID=?")) {
            stmnt.setString(1, printerUUID.toString());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                UUID ownerUUID = UUID.fromString(rs.getString("OwnerUUID"));
                String levelKey = rs.getString("level");
                String world = rs.getString("world");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int z = rs.getInt("z");

                double storedMoney = rs.getDouble("storedMoney");
                int timeUntilDestruction = rs.getInt("timeUntilDestruction");

                Location location = null;
                if (world != null) {
                    location = new Location(Bukkit.getWorld(world), x, y, z);
                }
                printer = new MoneyPrinter(printerUUID, PrinterType.getPrinterTypeMap().get(levelKey), ownerUUID, location);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return printer;
    }

    public MoneyPrinter getPrinterByLocation(Location location) {
        if (location == null) return null;
        MoneyPrinter printer = null;
        try (PreparedStatement stmnt = controller.getPreparedStatement("SELECT * FROM Printers WHERE world=? AND x=? AND y=? AND z=?")) {
            stmnt.setString(1, location.getWorld().getName());
            stmnt.setInt(2, location.getBlockX());
            stmnt.setInt(3, location.getBlockY());
            stmnt.setInt(4, location.getBlockZ());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                UUID printerUUID = UUID.fromString(rs.getString("PrinterUUID"));
                UUID ownerUUID = UUID.fromString(rs.getString("OwnerUUID"));
                String levelKey = rs.getString("level");
                String world = rs.getString("world");

                double storedMoney = rs.getDouble("storedMoney");
                int timeUntilDestruction = rs.getInt("timeUntilDestruction");

                printer = new MoneyPrinter(printerUUID, PrinterType.getPrinterTypeMap().get(levelKey), ownerUUID, location);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return printer;
    }

    synchronized public void removePrinter(UUID printerUUID) {
        try (PreparedStatement stmnt = controller.getPreparedStatement("DELETE FROM Printers WHERE PrinterUUID=?")) {
            stmnt.setString(1, printerUUID.toString());
            stmnt.executeUpdate();
            Debug.print("Removed Customer > "+printerUUID.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPrinterPlayerCount(UUID ownerUUID) {
        int count = 0;
        try (PreparedStatement stmnt = controller.getPreparedStatement("SELECT OwnerUUID, COUNT(*) AS printerCount FROM Printers WHERE OwnerUUID=?")) {
            stmnt.setString(1, ownerUUID.toString());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                count = rs.getInt("printerCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}

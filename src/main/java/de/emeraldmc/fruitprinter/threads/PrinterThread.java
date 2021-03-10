package de.emeraldmc.fruitprinter.threads;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.util.Debug;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class PrinterThread implements Runnable {

    private static HashMap<UUID, PrinterThread> threadPrinterHashMap = new HashMap<>();

    private int taskId = -1;
    private MoneyPrinter printer;

    public PrinterThread(MoneyPrinter printer) {
        this.printer = printer;
    }

    @Override
    public void run() {

            if (printer.getTimeUntilDestroy() <= printer.getLevel().getDestroyTime()/10) {
                printer.toggleBurn();
            }

            printer.addStoredMoney(printer.getLevel().getMoneyPerPeriod());
            printer.removeTimeUntilDestroy(printer.getLevel().getRefreshRate());
            printer.updateHolo();

            System.out.println("PRINTED MONEY! > "+printer.getStoredMoney());
            FruitPrinter.printerDB.updatePrinter(printer);
            if (printer.getTimeUntilDestroy() - printer.getLevel().getRefreshRate() <= 0) {
                stop();
                if (printer.getStoredMoney() <= 0) printer.destroy();
            }

    }

    public void start() {
        Debug.print("Starting new PrinterThread!");

        if (taskId == -1) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(FruitPrinter.getInstance(), this, printer.getLevel().getRefreshRate(), printer.getLevel().getDestroyTime());
            System.out.println(printer.getLevel().getRefreshRate());
            System.out.println(printer.getLevel().getDestroyTime());
            threadPrinterHashMap.put(printer.getUuid(), this);
        }
    }
    public void stop() {
        if (taskId != -1) Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
        threadPrinterHashMap.remove(printer.getUuid());
    }
}

package de.emeraldmc.fruitprinter.listener;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.objects.PrinterType;
import de.emeraldmc.fruitprinter.threads.PrinterThread;
import de.emeraldmc.fruitprinter.util.Debug;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PrinterPlaceEvent implements Listener {

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer() == null) return;

        Player p = e.getPlayer();
        ItemStack stack = p.getInventory().getItem(p.getInventory().getHeldItemSlot());
        UUID printerUUID = PrinterType.printerUUID(stack);
        if (printerUUID == null) return; // itemstack is no printer
        if (e.isCancelled()) return;

        System.out.println(e.getBlock().getLocation().toString());

        MoneyPrinter printer = FruitPrinter.printerDB.getPrinter(printerUUID);
        printer.setLocation(e.getBlockPlaced().getLocation());
        FruitPrinter.printerDB.updatePrinter(printer);
        PrinterThread thread = new PrinterThread(printer);
        printer.addHolo();
        thread.start();
    }
}

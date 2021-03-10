package de.emeraldmc.fruitprinter.listener;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.objects.PrinterType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class PrinterBreakEvent implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() != Material.PLAYER_HEAD) return;

        MoneyPrinter printer = FruitPrinter.printerDB.getPrinterByLocation(e.getBlock().getLocation());
        if (printer == null) return;
        if (!e.getPlayer().getUniqueId().equals(printer.getOwner())) e.setCancelled(true);
        e.setDropItems(false);
        printer.setLocation(null);
        FruitPrinter.printerDB.updatePrinter(printer);
        printer.removeHolo();
        e.getPlayer().getLocation().getWorld().dropItem(e.getPlayer().getLocation(), PrinterType.getMoneyPrinterItem(printer));
    }

    @EventHandler
    public void onBlockDamage(BlockExplodeEvent e) {
        if (e.blockList().stream().anyMatch(b -> b.getType() != Material.PLAYER_HEAD)) return;

        for (Block block : e.blockList()) {
            if (block.getType() == Material.PLAYER_HEAD) {
                MoneyPrinter printer = FruitPrinter.printerDB.getPrinterByLocation(block.getLocation());
                if (printer == null) continue;
                e.blockList().remove(block);
            }
        }
    }
}

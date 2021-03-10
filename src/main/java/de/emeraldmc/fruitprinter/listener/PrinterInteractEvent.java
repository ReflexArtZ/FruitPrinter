package de.emeraldmc.fruitprinter.listener;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.util.ChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PrinterInteractEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock().getType() != Material.PLAYER_HEAD) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            MoneyPrinter printer = FruitPrinter.printerDB.getPrinterByLocation(e.getClickedBlock().getLocation());
            if (printer == null) return;
            if (printer.getStoredMoney() <= 0) return;

            ChatAPI.sendMessage(Bukkit.getPlayer(printer.getOwner()), FruitPrinter.configuration.getEarnedMoneyMsg().replaceAll("%money%", String.valueOf(printer.getStoredMoney())));
            FruitPrinter.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(printer.getOwner()), printer.removeStoredMoney());

            if (printer.getTimeUntilDestroy() <= 0) {
                printer.destroy();
            }
        }
    }
}

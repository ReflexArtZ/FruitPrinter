package de.emeraldmc.fruitprinter.listener;

import de.emeraldmc.fruitprinter.objects.PrinterType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.UUID;

public class PrinterDropEvent implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() != Material.PLAYER_HEAD) return;
        UUID printerUUID = PrinterType.printerUUID(e.getItemDrop().getItemStack());
        if (printerUUID == null) return;
        e.setCancelled(true);
    }
}

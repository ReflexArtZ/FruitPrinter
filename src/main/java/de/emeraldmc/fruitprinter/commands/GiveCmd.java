package de.emeraldmc.fruitprinter.commands;

import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.objects.MoneyPrinter;
import de.emeraldmc.fruitprinter.objects.PrinterType;
import de.emeraldmc.fruitprinter.util.ChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GiveCmd implements CommandExecutor {

    // - /mp give [player] [printer] [amount]
    // - Permission: mp.give

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 4 || !args[0].equalsIgnoreCase("give")) return false;

        Player target = Bukkit.getPlayer(args[1]);
        String printer = args[2];
        int amount;

        // check if amount is numeric
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex) {
            return false;
        }

        // check online status of target
        if (target == null) {
            ChatAPI.sendMessage(sender, FruitPrinter.configuration.getPlayerNotOnlineMsg().replaceAll("%player%","NOT FOUND"));
            return true;
        }
        if (!target.isOnline()) {
            ChatAPI.sendMessage(sender, FruitPrinter.configuration.getPlayerNotOnlineMsg().replaceAll("%player%", target.getDisplayName()));
            return true;
        }

        // check if printer exists
        if (!PrinterType.getPrinterTypeMap().containsKey(printer)) return false;

        // check if player uses all printers
        if (FruitPrinter.printerDB.getPrinterPlayerCount(target.getUniqueId()) >= FruitPrinter.configuration.getMaxPrintersPerPlayer()) {
            ChatAPI.sendMessage(sender, FruitPrinter.configuration.getMaxPrintersMsg());
            return true;
        }

        PrinterType type = PrinterType.getPrinterTypeMap().get(printer);
        MoneyPrinter moneyPrinter = new MoneyPrinter(UUID.randomUUID(), type, target.getUniqueId(), null);

        ItemStack itemStack = PrinterType.getMoneyPrinterItem(moneyPrinter);

        for (int i = 0; i < amount; i++) {
            target.getInventory().addItem(itemStack);
        }

        FruitPrinter.printerDB.addPrinter(moneyPrinter);


        return true;
    }
}

package de.emeraldmc.fruitprinter.io;


import de.emeraldmc.fruitprinter.FruitPrinter;
import de.emeraldmc.fruitprinter.util.ChatAPI;

public class StandardConfiguration {
    private final boolean debug;
    private final String prefix;
    private final int maxPrintersPerPlayer;

    private final String giveMsg;
    private final String gotMsg;
    private final String earnedMoneyMsg;
    private final String destroyMsg;
    private final String playerNotOnlineMsg;
    private final String maxPrintersMsg;


    public StandardConfiguration() {
        prefix = getMessage("Config.prefix");
        debug = getBoolean("Config.debug");
        maxPrintersPerPlayer = getInt("Config.maxPrintersPerPlayer");

        giveMsg = getMessage("Config.Messages.giveMsg");
        gotMsg = getMessage("Config.Messages.gotMsg");
        earnedMoneyMsg = getMessage("Config.Messages.earnedMoneyMsg");
        destroyMsg = getMessage("Config.Messages.destroyMsg");
        playerNotOnlineMsg = getMessage("Config.Messages.playerNotOnlineMsg");
        maxPrintersMsg = getMessage("Config.Messages.maxPrintersMsg");
    }

    private String getMessage(String path) {
        return ChatAPI.translateColor(getString(path));
    }

    private int getInt(String path) {
        return FruitPrinter.getInstance().getConfig().getInt(path);
    }

    private double getDouble(String path) {
        return FruitPrinter.getInstance().getConfig().getDouble(path);
    }

    private String getString(String path) {
        return FruitPrinter.getInstance().getConfig().getString(path);
    }

    private boolean getBoolean(String path) {
        return FruitPrinter.getInstance().getConfig().getBoolean(path);
    }


    /**
     * Updates certain config values, values will not be saved if saveConfig() is not executed afterwards
     * To refresh the StandartConfiguration values see refresh()
     *
     * @param path
     * @param o
     */
    public void update(String path, Object o) {
        FruitPrinter.getInstance().getConfig().set(path, o);
    }

    public void saveConfig() {
        FruitPrinter.getInstance().saveConfig();
    }

    /**
     * Refreshes the StandartConfiguration values
     */
    public void refresh() {
        FruitPrinter.getInstance().reloadConfig();
        FruitPrinter.configuration = new StandardConfiguration();
    }

    // --- getter  ---

    public String getPrefix() {
        return prefix;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getGiveMsg() {
        return giveMsg;
    }

    public String getGotMsg() {
        return gotMsg;
    }

    public String getEarnedMoneyMsg() {
        return earnedMoneyMsg;
    }

    public String getDestroyMsg() {
        return destroyMsg;
    }

    public String getPlayerNotOnlineMsg() {
        return playerNotOnlineMsg;
    }

    public int getMaxPrintersPerPlayer() {
        return maxPrintersPerPlayer;
    }

    public String getMaxPrintersMsg() {
        return maxPrintersMsg;
    }
}


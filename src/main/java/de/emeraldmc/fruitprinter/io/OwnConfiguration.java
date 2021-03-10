package de.emeraldmc.fruitprinter.io;

import de.emeraldmc.fruitprinter.FruitPrinter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OwnConfiguration {
    private File configFile;
    private FileConfiguration configConfiguration;

    public OwnConfiguration(String name) {
        createFiles(name);
    }

    /**
     * Create independent configuration
     * @param name configuration-file name
     */
    private void createFiles(String name) {
        configFile = new File(FruitPrinter.getInstance().getDataFolder(), name);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            FruitPrinter.getInstance().saveResource(name, false);
        }

        configConfiguration = new YamlConfiguration();
        try {
            configConfiguration.load(configFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() throws IOException {
        getConfigConfiguration().save(configFile);
    }

    public FileConfiguration getConfigConfiguration() {
        return configConfiguration;
    }

    public File getConfig() {
        return configFile;
    }
}

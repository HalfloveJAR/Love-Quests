package me.Halflove.Quests.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class SettingsManager {
    static SettingsManager instance = new SettingsManager();

    static Plugin p;
    static FileConfiguration config;
    static FileConfiguration data;
    static File cfile;
    static File dfile;

    public static SettingsManager getInstance() {
        return instance;
    }

    public static String formatTime(long secs) {
        String output;
        long seconds = secs / 1000L;
        long minutes = 0L;
        while (seconds >= 60L) {
            seconds -= 60L;
            minutes++;
        }
        long hours = 0L;
        while (minutes >= 60L) {
            minutes -= 60L;
            hours++;
        }
        if (hours != 0L) {
            output = hours + " Hours " + minutes + " Minutes " + seconds + " Seconds";
        } else if (minutes != 0L) {
            if (seconds == 0L) {
                if (minutes == 1L) {
                    output = minutes + " Minute";
                } else {
                    output = minutes + " Minutes";
                }
            } else if (minutes == 1L) {
                if (seconds == 1L) {
                    output = minutes + " Minute " + seconds + " Second";
                } else {
                    output = minutes + " Minute " + seconds + " Seconds";
                }
            } else if (seconds == 1L) {
                output = minutes + " Minutes " + seconds + " Second";
            } else {
                output = minutes + " Minutes " + seconds + " Seconds";
            }
        } else if (seconds == 1L) {
            output = seconds + " Second";
        } else {
            output = seconds + " Seconds";
        }
        return output;
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public static FileConfiguration getData() {
        return data;
    }

    public static void saveData() {
        try {
            data.save(dfile);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save data.yml!");
        }
    }

    public static void reloadData() {
        data = YamlConfiguration.loadConfiguration(dfile);
    }

    public void setup(Plugin p) {
        if (!p.getDataFolder().exists())
            p.getDataFolder().mkdir();
        cfile = new File(p.getDataFolder(), "config.yml");
        if (!cfile.exists())
            try {
                cfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create config.yml!");
            }
        config = p.getConfig();
        config.options().copyDefaults(true);
        dfile = new File(p.getDataFolder(), "data.yml");
        if (!dfile.exists())
            try {
                dfile.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create data.yml!");
            }
        data = YamlConfiguration.loadConfiguration(dfile);
    }
}


package me.Halflove.Quests.Main;

import me.Halflove.Quests.Utils.QCommands;
import me.Halflove.Quests.Utils.QGui;
import me.Halflove.Quests.Utils.QListeners;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Plugin plugin;
    private static Economy econ = null;

    private static Permission perms = null;
    public SettingsManager settings = SettingsManager.getInstance();

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPerms() {
        return perms;
    }

    public void onEnable() {
        plugin = this;
        getLogger().info("Version " + getDescription().getVersion() + " has been enabled.");
        this.settings.setup(this);
        registerEvents();
        registerCommands();
        setupEconomy();
        setupPermissions();
    }

    public void onDisable() {
        getLogger().info("Version " + getDescription().getVersion() + " has been disabled.");
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new QGui(), this);
        Bukkit.getPluginManager().registerEvents(new QListeners(), this);
    }

    public void registerCommands() {
        getCommand("tasks").setExecutor(new QCommands());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return (econ != null);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return (perms != null);
    }
}


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
    public SettingsManager settings = SettingsManager.getInstance();

    private static Economy econ = null;

    private static Permission perms = null;

    public static Plugin plugin;

    public void onEnable() {
        /*Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (QGui.guiOpened.get(player)!=null) {
                        QGui.setGui(player);
                        player.updateInventory();
                        Bukkit.broadcastMessage("test");
                    }
                }
            }
        }, 1L, 100L);*/
        plugin = (Plugin)this;
        getLogger().info("Version " + getDescription().getVersion() + " has been enabled.");
        this.settings.setup((Plugin)this);
        registerEvents();
        registerCommands();
        setupEconomy();
        setupPermissions();
    }

    public void onDisable() {
        getLogger().info("Version " + getDescription().getVersion() + " has been disabled.");
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents((Listener)new QGui(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new QListeners(), (Plugin)this);
    }

    public void registerCommands() {
        getCommand("quests").setExecutor((CommandExecutor)new QCommands());
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = (Economy)rsp.getProvider();
        return (econ != null);
    }

    public static Economy getEconomy() {
        return econ;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = (Permission)rsp.getProvider();
        return (perms != null);
    }

    public static Permission getPerms() {
        return perms;
    }
}


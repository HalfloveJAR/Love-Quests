package me.Halflove.Quests.Utils;

import me.Halflove.Quests.Main.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QCommands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tasks"))
            if (sender.hasPermission("quests.admin")) {
                if (args.length == 0) {
                    QGui.openGui((Player)sender);
                } else if (args[0].equalsIgnoreCase("reload")) {
                    SettingsManager.reloadConfig();
                    SettingsManager.reloadData();
                    sender.sendMessage(ChatColor.GREEN + "Quests config and data reloaded");
                } else if (args[0].equalsIgnoreCase("debug")) {
                    QManager.debug(sender);
                } else if (args[0].equalsIgnoreCase("update")) {
                    // QManager.leaderboard();
                    sender.sendMessage(ChatColor.GREEN + "Force updated the leaderboard");
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if(args.length == 1){

                    }else if(args.length == 2){
                        Player player = Bukkit.getPlayer(args[1]);
                        if(Bukkit.getOnlinePlayers().contains(player)){
                            sender.sendMessage(ChatColor.GREEN + "Reset all quest cooldowns for " + player.getName());
                            QManager.resetCooldown(player,"quest1");
                            QManager.resetCooldown(player,"quest2");
                            QManager.resetCooldown(player,"quest3");
                        }else{
                            sender.sendMessage(ChatColor.RED + "Player not found");
                        }
                    }else{
                        sender.sendMessage(ChatColor.RED + "Invalid args. /quests reset [name]");
                    }
                } else if(args[0].equalsIgnoreCase("progress")){
                    Player player = (Player) sender;
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
                    QManager.getProgressMessage((Player) sender, QManager.getQuestId((Player) sender));
                }else{
                    QGui.openGui((Player)sender);
                }
            } else if(args.length != 1){
                Player player = (Player) sender;
                if(!player.getLocation().getWorld().getName().equalsIgnoreCase("spawn") && !player.hasPermission("unlocked.12")) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
                    sender.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fOops, you can only do &e/q progress&f in this world!"));
                    sender.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fUnlock the remote menu at &elevel 12&f! (&e/ranks&f)"));
                }else{
                    QGui.openGui(player);
                }
            }else if(args[0].equalsIgnoreCase("progress")){
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
                QManager.getProgressMessage((Player) sender, QManager.getQuestId((Player) sender));
            }else{
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
                sender.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fOops, you typed that wrong!"));
                sender.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fTry /Quests or /Quests Progress"));
            }
        return true;
    }
}


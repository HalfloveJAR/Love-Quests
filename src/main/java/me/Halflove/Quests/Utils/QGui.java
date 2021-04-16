package me.Halflove.Quests.Utils;

import me.Halflove.Quests.Main.SettingsManager;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QGui implements Listener {
    public static DecimalFormat format = new DecimalFormat("#.##");
    //public static HashMap<Player, Boolean> guiOpened = new HashMap<Player, Boolean>();

    public static void openGui(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Quests");
        //guiOpened.put(player, true);
        if (!QManager.getQuestList().contains(QManager.getQuestOne(player)))
            QManager.assignQuest(player, "1");
        if (SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest1.id") == null &&
                !QManager.isComplete(player, "quest1"))
            QManager.assignQuest(player, "1");
        String id1 = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest1.id");
        if (!QManager.getQuestList().contains(QManager.getQuestTwo(player)))
            QManager.assignQuest(player, "2");
        if (SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest2.id") == null &&
                !QManager.isComplete(player, "quest2"))
            QManager.assignQuest(player, "2");
        String id2 = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest2.id");
        if (!QManager.getQuestList().contains(QManager.getQuestThree(player)))
            QManager.assignQuest(player, "3");
        if (SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest3.id") == null &&
                !QManager.isComplete(player, "quest3"))
            QManager.assignQuest(player, "3");
        String id3 = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest3.id");
        if (QManager.isComplete(player, "quest1")) {
            long cooldown = QManager.getCooldown(player, "quest1");
            long difference = cooldown - System.currentTimeMillis();
            ItemStack quest1 = new ItemStack(Material.BOOK);
            quest1.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
            ItemMeta quest1meta = quest1.getItemMeta();
            quest1meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 1&8] &7âž­ &c&lUnavailable"));
            List<String> lore1 = SettingsManager.getConfig().getStringList("quests." + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&c&l  Under cooldown"));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "This quest is currently under cooldown"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + SettingsManager.formatTime(difference) + " until reset"));
            lore1.add("");
            if (QManager.isDonor(player)) {
                lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 50%"));
                lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because of your status as a donor"));
            } else if  (QManager.isCDQualified(player)) {
                if (player.hasPermission("unlocked.20")) {
                    lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 50%"));
                    lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 20"));
                } else if (player.hasPermission("unlocked.10")) {
                    lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 15%"));
                    lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 10"));
                } else if (player.hasPermission("unlocked.3")) {
                    lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 5%"));
                    lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 3"));
                }
            }else{
                lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Consider upgrading for"));
                lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "reduced quest cooldown times"));
                lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Use &e/ranks &7or &e/buy"));
            }
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &a" + "Click here to automatically"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &a" + "reset your cooldown for &2&l$" + format.format(QManager.getResetMoney(player, "quest1"))));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            quest1meta.setLore(lore1);
            quest1.setItemMeta(quest1meta);
            gui.setItem(12, quest1);
        } else if (QManager.hasQuest(player) && QManager.getQuest(player).equals("quest1")) {
            ItemStack quest1 = new ItemStack(Material.WRITABLE_BOOK);
            quest1.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
            ItemMeta quest1meta = quest1.getItemMeta();
            quest1meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 1&8] &7âž­ &a&lSelected"));
            List<String> lore1 = SettingsManager.getConfig().getStringList("quests." + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&a&l  Currently selected"));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestOne(player))));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestOne(player)).get(0)));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestOne(player)).get(1)));
            lore1.add("");
            lore1.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            quest1meta.setLore(lore1);
            quest1.setItemMeta(quest1meta);
            gui.setItem(12, quest1);
        } else if (QManager.hasQuest(player)) {
            ItemStack quest1 = new ItemStack(Material.BOOK);
            ItemMeta quest1meta = quest1.getItemMeta();
            quest1meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 1&8] &7âž­ &c&lUnavailable"));
            List<String> lore1 = SettingsManager.getConfig().getStringList("quests." + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&c&l  You already have a quest"));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestOne(player))));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestOne(player)).get(0)));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestOne(player)).get(1)));
            lore1.add("");
            lore1.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            quest1meta.setLore(lore1);
            quest1.setItemMeta(quest1meta);
            gui.setItem(12, quest1);
        } else {
            ItemStack quest1 = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta quest1meta = quest1.getItemMeta();
            quest1meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 1&8] &7âž­ &e&lAvailable"));
            List<String> lore1 = SettingsManager.getConfig().getStringList("quests." + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&b&l  Click to select"));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestOne(player))));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestOne(player)).get(0)));
            lore1.add("");
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
            lore1.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestOne(player)).get(1)));
            lore1.add("");
            lore1.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id1);
            lore1.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            quest1meta.setLore(lore1);
            quest1.setItemMeta(quest1meta);
            gui.setItem(12, quest1);
        }
        if(player.hasPermission("unlocked.5")) {
            if (QManager.isComplete(player, "quest2")) {
                long cooldown = QManager.getCooldown(player, "quest2");
                long difference = cooldown - System.currentTimeMillis();
                ItemStack quest2 = new ItemStack(Material.BOOK);
                quest2.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
                ItemMeta quest2meta = quest2.getItemMeta();
                quest2meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 2&8] &7âž­ &c&lUnavailable"));
                List<String> lore2 = SettingsManager.getConfig().getStringList("quests." + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&c&l  Under cooldown"));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "This quest is currently under cooldown"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + SettingsManager.formatTime(difference) + " until reset"));
                lore2.add("");
                if (QManager.isDonor(player)) {
                    lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 50%"));
                    lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because of your status as a donor"));
                } else if  (QManager.isCDQualified(player)) {
                    if (player.hasPermission("unlocked.20")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 50%"));
                        lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 20"));
                    } else if (player.hasPermission("unlocked.10")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 15%"));
                        lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 10"));
                    } else if (player.hasPermission("unlocked.3")) {
                        lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 5%"));
                        lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 3"));
                    }
                }else{
                    lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Consider upgrading for"));
                    lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "reduced quest cooldown times"));
                    lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Use &e/ranks &7or &e/buy"));
                }
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &a" + "Click here to automatically"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &a" + "reset your cooldown for &2&l$" + format.format(QManager.getResetMoney(player, "quest2"))));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest2meta.setLore(lore2);
                quest2.setItemMeta(quest2meta);
                gui.setItem(13, quest2);
            } else if (QManager.hasQuest(player) && QManager.getQuest(player).equals("quest2")) {
                ItemStack quest2 = new ItemStack(Material.WRITABLE_BOOK);
                quest2.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
                ItemMeta quest2meta = quest2.getItemMeta();
                quest2meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 2&8] &7âž­ &a&lSelected"));
                List<String> lore2 = SettingsManager.getConfig().getStringList("quests." + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&a&l  Currently selected"));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestTwo(player))));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestTwo(player)).get(0)));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestTwo(player)).get(1)));
                lore2.add("");
                lore2.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest2meta.setLore(lore2);
                quest2.setItemMeta(quest2meta);
                gui.setItem(13, quest2);
            } else if (QManager.hasQuest(player)) {
                ItemStack quest2 = new ItemStack(Material.BOOK);
                ItemMeta quest2meta = quest2.getItemMeta();
                quest2meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 2&8] &7âž­ &c&lUnavailable"));
                List<String> lore2 = SettingsManager.getConfig().getStringList("quests." + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&c&l  You already have a quest"));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestTwo(player))));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestTwo(player)).get(0)));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestTwo(player)).get(1)));
                lore2.add("");
                lore2.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest2meta.setLore(lore2);
                quest2.setItemMeta(quest2meta);
                gui.setItem(13, quest2);
            } else {
                ItemStack quest2 = new ItemStack(Material.WRITABLE_BOOK);
                ItemMeta quest2meta = quest2.getItemMeta();
                quest2meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 2&8] &7âž­ &e&lAvailable"));
                List<String> lore2 = SettingsManager.getConfig().getStringList("quests." + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&b&l  Click to select"));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestTwo(player))));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestTwo(player)).get(0)));
                lore2.add("");
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
                lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestTwo(player)).get(1)));
                lore2.add("");
                lore2.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id2);
                lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest2meta.setLore(lore2);
                quest2.setItemMeta(quest2meta);
                gui.setItem(13, quest2);
            }
        }else{
            ItemStack quest2 = new ItemStack(Material.GRAY_DYE);
            ItemMeta quest2meta = quest2.getItemMeta();
            quest2meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 2&8] &7âž­ &4&lLocked"));
            List<String> lore2 = SettingsManager.getConfig().getStringList("quests." + id2);
            lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            lore2.add(ChatColor.translateAlternateColorCodes('&', "&c&l  You haven't unlocked this yet"));
            lore2.add("");
            lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Unlock this quest slot at Level 5"));
            lore2.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Progress through the levels with /ranks"));
            lore2.add("");
            lore2.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            quest2meta.setLore(lore2);
            quest2.setItemMeta(quest2meta);
            gui.setItem(13, quest2);
        }
        if(player.hasPermission("unlocked.15")) {
            if (QManager.isComplete(player, "quest3")) {
                long cooldown = QManager.getCooldown(player, "quest3");
                long difference = cooldown - System.currentTimeMillis();
                ItemStack quest3 = new ItemStack(Material.BOOK);
                quest3.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
                ItemMeta quest3meta = quest3.getItemMeta();
                quest3meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 3&8] &7âž­ &c&lUnavailable"));
                List<String> lore3 = SettingsManager.getConfig().getStringList("quests." + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&c&l  Under cooldown"));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "This quest is currently under cooldown"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + SettingsManager.formatTime(difference) + " until reset"));
                lore3.add("");
                if (QManager.isDonor(player)) {
                    lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 50%"));
                    lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because of your status as a donor"));
                } else if  (QManager.isCDQualified(player)) {
                    if (player.hasPermission("unlocked.20")) {
                        lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 50%"));
                        lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 20"));
                    } else if (player.hasPermission("unlocked.10")) {
                        lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 15%"));
                        lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 10"));
                    } else if (player.hasPermission("unlocked.3")) {
                        lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Your cooldown is reduced by 5%"));
                        lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "because you reached level 3"));
                    }
                }else{
                    lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Consider upgrading for"));
                    lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "reduced quest cooldown times"));
                    lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + "Use &e/ranks &7or &e/buy"));
                }
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &a" + "Click here to automatically"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &a" + "reset your cooldown for &2&l$" + format.format(QManager.getResetMoney(player, "quest3"))));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest3meta.setLore(lore3);
                quest3.setItemMeta(quest3meta);
                gui.setItem(14, quest3);
            } else if (QManager.hasQuest(player) && QManager.getQuest(player).equals("quest3")) {
                ItemStack quest3 = new ItemStack(Material.WRITABLE_BOOK);
                quest3.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 0);
                ItemMeta quest3meta = quest3.getItemMeta();
                quest3meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 3&8] &7âž­ &a&lSelected"));
                List<String> lore3 = SettingsManager.getConfig().getStringList("quests." + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&a&l  Currently selected"));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestThree(player))));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestThree(player)).get(0)));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestThree(player)).get(1)));
                lore3.add("");
                lore3.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest3meta.setLore(lore3);
                quest3.setItemMeta(quest3meta);
                gui.setItem(14, quest3);
            } else if (QManager.hasQuest(player)) {
                ItemStack quest3 = new ItemStack(Material.BOOK);
                ItemMeta quest3meta = quest3.getItemMeta();
                quest3meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 3&8] &7âž­ &c&lUnavailable"));
                List<String> lore3 = SettingsManager.getConfig().getStringList("quests." + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&c&l  You already have a quest"));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestThree(player))));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestThree(player)).get(0)));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestThree(player)).get(1)));
                lore3.add("");
                lore3.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest3meta.setLore(lore3);
                quest3.setItemMeta(quest3meta);
                gui.setItem(14, quest3);
            } else {
                ItemStack quest3 = new ItemStack(Material.WRITABLE_BOOK);
                ItemMeta quest3meta = quest3.getItemMeta();
                quest3meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 3&8] &7âž­ &e&lAvailable"));
                List<String> lore3 = SettingsManager.getConfig().getStringList("quests." + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&b&l  Click to select"));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&d&l"+QManager.getName(QManager.getQuestThree(player))));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&6&lObjective:"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestThree(player)).get(0)));
                lore3.add("");
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&6&lReward:"));
                lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7" + (String)QManager.getObjective(QManager.getQuestThree(player)).get(1)));
                lore3.add("");
                lore3.add(ChatColor.GRAY +""+ ChatColor.ITALIC + "id: " + id3);
                lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
                quest3meta.setLore(lore3);
                quest3.setItemMeta(quest3meta);
                gui.setItem(14, quest3);
            }
        }else{
            ItemStack quest3 = new ItemStack(Material.GRAY_DYE);
            ItemMeta quest3meta = quest3.getItemMeta();
            quest3meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8[&6Quest 3&8] &7âž­ &4&lLocked"));
            List<String> lore3 = SettingsManager.getConfig().getStringList("quests." + id2);
            lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&c&l  You haven't unlocked this yet"));
            lore3.add("");
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Unlock this quest slot at Level 15"));
            lore3.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Progress through the levels with /ranks"));
            lore3.add("");
            lore3.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
            quest3meta.setLore(lore3);
            quest3.setItemMeta(quest3meta);
            gui.setItem(14, quest3);
        }
        ItemStack reroll = new ItemStack(Material.LEGACY_EMPTY_MAP);
        ItemMeta rerollmeta = reroll.getItemMeta();
        rerollmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lReroll"));
        ArrayList<String> rerolllore = new ArrayList<>();
        rerolllore.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
        rerolllore.add(ChatColor.translateAlternateColorCodes('&',"&a&l  Click to reroll"));
        rerolllore.add("");
        rerolllore.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Cancel all your current quests and"));
        rerolllore.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7your currently selected quest."));
        rerolllore.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Does not reset cooldowns."));
        rerolllore.add("");
        rerolllore.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7Price: $"+(int)QManager.getRerollPrice(player)));
        rerolllore.add("");
        rerolllore.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
        rerollmeta.setLore(rerolllore);
        reroll.setItemMeta(rerollmeta);
        gui.setItem(10, reroll);
        ItemStack stats = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        int completed = SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".quests-completed");
        SkullMeta statsmeta = (SkullMeta)stats.getItemMeta();
        statsmeta.setOwner(player.getName());
        statsmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lQuests Completed"));
        ArrayList<String> statslore = new ArrayList<>();
        statslore.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
        statslore.add("");
        statslore.add(ChatColor.translateAlternateColorCodes('&', "&e Â» &7You have completed &e" + completed + "&7 quests"));
        statslore.add("");
        statslore.add(ChatColor.translateAlternateColorCodes('&',"&8&l&mâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Žâ¥Ž"));
        statsmeta.setLore(statslore);
        stats.setItemMeta((ItemMeta)statsmeta);
        gui.setItem(16, stats);
        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.5F, 1.0F);
        SettingsManager.saveData();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Quests")) {
            e.setCancelled(true);
            Player player = (Player)e.getWhoClicked();
            if (!e.getCurrentItem().getType().equals(Material.AIR))
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Quest 1")) {
                    if (!QManager.isComplete(player, "quest1")) {
                        QManager.selectQuest(player, "quest1", QManager.getQuestOne(player));
                    } else {
                        QManager.buyReset(player, "quest1");
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Quest 2")) {
                    if (!QManager.isComplete(player, "quest2")) {
                        QManager.selectQuest(player, "quest2", QManager.getQuestTwo(player));
                    } else {
                        QManager.buyReset(player, "quest2");
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Quest 3")) {
                    if (!QManager.isComplete(player, "quest3")) {
                        QManager.selectQuest(player, "quest3", QManager.getQuestThree(player));
                    } else {
                        QManager.buyReset(player, "quest3");
                    }
                } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Reroll")) {
                    QManager.rerollQuests(player);
                } /*else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Quests Completed")) {
                    //player.sendMessage(ChatColor.YELLOW + "Visit " + ChatColor.GOLD + "/warp quests " + ChatColor.YELLOW + "to view the top 10 questers leaderboard");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
                    player.closeInventory();
                } */else {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
                }
        }
    }
}



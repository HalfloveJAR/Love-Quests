package me.Halflove.Quests.Utils;

import me.Halflove.Quests.Main.Main;
import me.Halflove.Quests.Main.SettingsManager;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QManager {

    public static void assignQuest(Player player, String number) {
        ArrayList<String> quests = new ArrayList<>();
        for (String quest : SettingsManager.getConfig().getConfigurationSection("quests").getKeys(false))
            quests.add(quest);
        Random random = new Random();
        int preselected = random.nextInt(quests.size());
        String selected = quests.get(preselected);
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".quest" + number + ".id", selected);
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".quest" + number + ".cooldown", null);
        SettingsManager.saveData();
    }

    public static void selectQuest(Player player, String quest, String id) {
        if (hasQuest(player)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou already have a quest selected"));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
        } else {
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id", quest);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount", Integer.valueOf(0));
            SettingsManager.saveData();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fSelected quest: " + getName(id)));
            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 10.0F, 1.0F);
            player.closeInventory();
            QGui.openGui(player);
        }
    }

    ///////////////////
    public static void finishQuest(Player player, String quest) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fQuest Completed!"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + getName(getQuestId(player)) + " completed, delivering rewards!"));
        for (String reward : SettingsManager.getConfig().getStringList("quests." + getQuestId(player) + ".rewards")) {
            reward = reward.replace("%player%", player.getName());
            if (player.getInventory().firstEmpty() != -1) {
                Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), reward);
                continue;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYour inventory is full, here, take a virtual crate key instead! (Invisible key)"));
            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 10.0F, 1.0F);
            Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), reward.replace(" p ", " v "));
        }
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.5F, 2.0F);

        if(SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id").equals("quest1")) {
            assignQuest(player, "1");
        }else if(SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id").equals("quest2")) {
            assignQuest(player, "2");
        }else{
            assignQuest(player, "3");
        }
        setCooldown(player, getQuest(player));
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id", null);
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount", null);
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest", null);
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + "." + quest, null);
        int total = SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".quests-completed");
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".quests-completed", Integer.valueOf(total + 1));
        SettingsManager.saveData();
    }

    public static void testComplete(Player player, String id) {
        DecimalFormat format = new DecimalFormat("#.##");
        double end = SettingsManager.getConfig().getInt("quests." + id + ".amount");
        double current = SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount");
        double progress = current / end * 100.0D;
        if (current >= end) {
            finishQuest(player, id);
        } else if (end < 9.0D) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + getName(id) + " Quest Progress"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" +""+ (int)current + "/" + (int)end + " (" + format.format(progress) + "% Complete)"));
        } else if (progress == 25.0D) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + getName(id) + " Quest Progress"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" +""+ (int)current + "/" + (int)end + " (" + format.format(progress) + "% Complete)"));
        } else if (progress == 50.0D) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + getName(id) + " Quest Progress"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" +""+ (int)current + "/" + (int)end + " (" + format.format(progress) + "% Complete)"));
        } else if (progress == 75.0D) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + getName(id) + " Quest Progress"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" +""+ (int)current + "/" + (int)end + " (" + format.format(progress) + "% Complete)"));
        }
    }

    public static void getProgressMessage(Player player, String id) {
        if (hasQuest(player)) {
            DecimalFormat format = new DecimalFormat("#.##");
            double end = SettingsManager.getConfig().getInt("quests." + id + ".amount");
            double current = SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount");
            double progress = current / end * 100.0D;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + getName(id) + " Quest Progress"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &f" + "" + (int) current + "/" + (int) end + " (" + format.format(progress) + "% Complete)"));
        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou are not currently doing a quest"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fSelect a quest at &e/warp quests&f!"));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
        }
    }

    public static double getRerollPrice(Player player){
        double price = 0.0;
        if(player.hasPermission("unlocked.15")){
            price = 4500.0;
        }else if(player.hasPermission("unlocked.5")){
            price = 3000.0;
        }else{
            price = 1500.0;
        }
        return price;
    }

    public static void rerollQuests(Player player) {
        long current = System.currentTimeMillis();
        long one = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + ".quest1.cooldown");
        long two = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + ".quest2.cooldown");
        long three = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + ".quest3.cooldown");
        if (one > current && two > current && three > current) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou don't have any available quests to reroll"));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0F, 1.0F);
        } else if (Main.getEconomy().getBalance((OfflinePlayer)player) >= getRerollPrice(player)) {
            Main.getEconomy().withdrawPlayer((OfflinePlayer)player, getRerollPrice(player));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou rerolled your quests for $"+(int)getRerollPrice(player)));
            ArrayList<String> quests = new ArrayList<>();
            for (String quest : SettingsManager.getConfig().getConfigurationSection("quests").getKeys(false))
                quests.add(quest);
            Random random = new Random();
            int preselected = random.nextInt(quests.size());
            String selected = quests.get(preselected);
            int preselected2 = random.nextInt(quests.size());
            String selected2 = quests.get(preselected2);
            int preselected3 = random.nextInt(quests.size());
            String selected3 = quests.get(preselected3);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id", null);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount", null);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".selected-quest", null);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".quest1.id", selected);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".quest2.id", selected2);
            SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + ".quest3.id", selected3);
            SettingsManager.saveData();
            player.closeInventory();
            QGui.openGui(player);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou don't have enough money for that"));
        }
    }

    public static String getProgressGUI(Player player, String id) {
        DecimalFormat format = new DecimalFormat("#.##");
        double end = SettingsManager.getConfig().getInt("quests." + id + ".amount");
        double current = SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount");
        double progress = current / end * 100.0D;
        return (int)current + "/" + (int)end + " (" + format.format(progress) + "% Complete)";
    }

    public static void setCooldown(Player player, String quest) {
        long cooldown = 0L;
        long current = System.currentTimeMillis();
        if (Main.getPerms().playerInGroup(player, "Hero")) {
            cooldown = 10530608L;
        }else if (Main.getPerms().playerInGroup(player, "Legend")) {
            cooldown = 10530608L;
        }else if (Main.getPerms().playerInGroup(player, "Master")) {
            cooldown = 10530608L;
        }else if (player.hasPermission("unlocked.20")) {
            cooldown = 10530608L;
        } else if (player.hasPermission("unlocked.10")) {
            cooldown = 18314100L;
        } else if (player.hasPermission("unlocked.3")) {
            cooldown = 20520000L;
        } else {
            cooldown = 43200000L;
        }
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + "." + quest + ".cooldown", Long.valueOf(cooldown + current));
        SettingsManager.saveData();
    }

    public static void resetCooldown(Player player, String quest) {
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + "." + quest + ".cooldown", Integer.valueOf(0));
        SettingsManager.getData().set(String.valueOf(player.getUniqueId().toString()) + "." + quest + ".cooldown", null);
        SettingsManager.saveData();
    }

    public static void buyReset(Player player, String quest) {
        DecimalFormat format = new DecimalFormat("#.##");
        if (getResetMoney(player, quest) < 1.0D) {
            player.closeInventory();
            QGui.openGui(player);
        } else if (Main.getEconomy().getBalance((OfflinePlayer)player) >= getResetMoney(player, quest)) {
            Main.getEconomy().withdrawPlayer((OfflinePlayer)player, getResetMoney(player, quest));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou reset your quest cooldown for $" + format.format(getResetMoney(player, quest)) ));
            resetCooldown(player, quest);
            SettingsManager.saveData();
            assignQuest(player, quest.replace("quest", ""));
            player.closeInventory();
            QGui.openGui(player);
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8(&6&l!&8) &6&lQuests: &fYou don't have enough money for that"));
        }
    }

    public static double getResetMoney(Player player, String quest) {
        double output = 0.0D;
        long current = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + "." + quest + ".cooldown") - System.currentTimeMillis();
        double hours = current / 3600000.0D;
        output = hours * 1000.0D;
        return output;
    }

    public static String formatResetMoney(Player player, double raw) {
        String price = String.valueOf(raw);
        Bukkit.broadcastMessage(price.split(".")[0]);
        int length = price.length() - 3;
        String output = "";
        if (length > 3) {
            output = String.valueOf(price.split(".")[0].substring(0, length - 3)) + "," + price.split(".")[0].substring(length, -3) + price.split(".")[1];
        } else {
            output = price;
        }
        return output;
    }

    public static long getCooldown(Player player, String quest) {
        return SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + "." + quest + ".cooldown");
    }

    public static boolean isDonor(Player player) {
        boolean output = false;
        if (Main.getPerms().playerInGroup(player, "Hero")) {
            output = true;
        }else if (Main.getPerms().playerInGroup(player, "Legend")) {
            output = true;
        }else if (Main.getPerms().playerInGroup(player, "Master")) {
            output = true;
        }
        return output;
    }

    public static boolean isCDQualified(Player player) {
        boolean output = false;
        if (player.hasPermission("unlocked.20")) {
            output = true;
        } else if (player.hasPermission("unlocked.10")) {
            output = true;
        } else if (player.hasPermission("unlocked.3")) {
            output = true;
        }
        return output;
    }

    public static boolean isComplete(Player player, String quest) {
        boolean output = false;
        long cooldown = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + "." + quest + ".cooldown");
        long current = System.currentTimeMillis();
        if (cooldown > current)
            output = true;
        return output;
    }

    public static boolean hasAvailable(Player player) {
        boolean output = true;
        long current = System.currentTimeMillis();
        long one = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + ".quest1.cooldown");
        long two = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + ".quest2.cooldown");
        long three = SettingsManager.getData().getLong(String.valueOf(player.getUniqueId().toString()) + ".quest3.cooldown");
        if (hasQuest(player))
            output = false;
        if (one > current && two > current && three > current)
            output = false;
        return output;
    }

    /*public static void leaderboard() {
        List<String> top = new ArrayList<>();
        byte b;
        int i;
        OfflinePlayer[] arrayOfOfflinePlayer;
        for (i = (arrayOfOfflinePlayer = Bukkit.getServer().getOfflinePlayers()).length, b = 0; b < i; ) {
            OfflinePlayer player = arrayOfOfflinePlayer[b];
            top.add(String.valueOf(SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".quests-completed")) + ";" + player.getName());
            b++;
        }
        Collections.sort(top, new QComparator());
        for (Hologram old : HologramsAPI.getHolograms(Main.plugin))
            old.delete();
        Location location = new Location(Bukkit.getWorld("newwild"), -9140.98D, 121.0D, 5709.555D);
        Hologram hologram = HologramsAPI.createHologram(Main.plugin, location);
        hologram.appendTextLine(ChatColor.GOLD +""+ ChatColor.BOLD + "Top Questers");
        hologram.appendTextLine(ChatColor.GRAY +""+ ChatColor.STRIKETHROUGH + "-----------------");
        hologram.appendTextLine(ChatColor.WHITE + "1. " + ((String)top.get(0)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(0)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "2. " + ((String)top.get(1)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(1)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "3. " + ((String)top.get(2)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(2)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "4. " + ((String)top.get(3)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(3)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "5. " + ((String)top.get(4)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(4)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "6. " + ((String)top.get(5)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(5)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "7. " + ((String)top.get(6)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(6)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "8. " + ((String)top.get(7)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(7)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "9. " + ((String)top.get(8)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(8)).split(";")[0]);
        hologram.appendTextLine(ChatColor.WHITE + "10. " + ((String)top.get(9)).split(";")[1] + " - " + ChatColor.AQUA + ((String)top.get(9)).split(";")[0]);
    }*/

    public static void debug(CommandSender sender) {
        List<String> top = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (!top.contains(String.valueOf(SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".quests-completed")) + ";" + player.getName()))
                top.add(String.valueOf(SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".quests-completed")) + ";" + player.getName());
        }
        Collections.sort(top, Collections.reverseOrder());
        sender.sendMessage(top + " online debug");
    }

    public static double getProgress(Player player, String quest) {
        double output = 0.0D;
        String id = "";
        if (getQuest(player).equals("quest1")) {
            id = getQuestOne(player);
        } else if (getQuest(player).equals("quest2")) {
            id = getQuestTwo(player);
        } else {
            id = getQuestThree(player);
        }
        double end = SettingsManager.getConfig().getInt("quests." + id + ".amount");
        double current = SettingsManager.getData().getInt(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.amount");
        output = current / end * 100.0D;
        return output;
    }

    public static String getQuestId(Player player) {
        String output = "";
        if (getQuest(player).equals("quest1")) {
            output = getQuestOne(player);
        } else if (getQuest(player).equals("quest2")) {
            output = getQuestTwo(player);
        } else {
            output = getQuestThree(player);
        }
        return output;
    }

    public static boolean hasQuest(Player player) {
        boolean output = false;
        if (SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id") != null)
            output = true;
        return output;
    }

    public static String getQuest(Player player) {
        String output = "";
        if (hasQuest(player))
            output = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".selected-quest.id");
        return output;
    }

    public static String getQuestOne(Player player) {
        String output = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest1.id");
        return output;
    }

    public static String getQuestTwo(Player player) {
        String output = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest2.id");
        return output;
    }

    public static String getQuestThree(Player player) {
        String output = SettingsManager.getData().getString(String.valueOf(player.getUniqueId().toString()) + ".quest3.id");
        return output;
    }

    public static String getName(String quest) {
        String output = SettingsManager.getConfig().getString("quests." + quest + ".name");
        return output;
    }

    public static ArrayList<String> getLore(String quest) {
        ArrayList<String> output = new ArrayList<>();
        return output;
    }

    public static String getDifficulty(String quest) {
        String output = SettingsManager.getConfig().getString("quests." + quest + ".difficulty");
        return output;
    }

    public static String getObjectiveType(String quest) {
        String output = SettingsManager.getConfig().getString("quests." + quest + ".objective-type");
        return output;
    }

    public static List<String> getObjective(String quest) {
        List<String> output = SettingsManager.getConfig().getStringList("quests." + quest + ".objective");
        return output;
    }

    public static Material getMaterial(String quest) {
        return Material.valueOf(SettingsManager.getConfig().getString("quests." + quest + ".type"));
    }

    public static String getType(String quest) {
        return SettingsManager.getConfig().getString("quests." + quest + ".type");
    }

    public static ArrayList<String> getRewards(String quest) {
        ArrayList<String> output = new ArrayList<>();
        return output;
    }

    public static List<String> getQuestList() {
        List<String> quests = new ArrayList<>();
        for (String quest : SettingsManager.getConfig().getConfigurationSection("quests").getKeys(false))
            quests.add(quest);
        return quests;
    }
}
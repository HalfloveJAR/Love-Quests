package me.Halflove.Quests.Utils;

import me.Halflove.Quests.Main.Main;
import me.Halflove.Quests.Main.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class QListeners implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        block.setMetadata("placed", new FixedMetadataValue(Main.plugin, "true"));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String quest = QManager.getQuestId(player);
        if (!QManager.hasAvailable(player)) {
            if (event.getBlock().hasMetadata("placed")) {
                event.getBlock().removeMetadata("placed", Main.plugin);
            } else if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("mine") &&
                    event.getBlock().getType().equals(Material.valueOf(QManager.getMaterial(QManager.getQuestId(player)).toString()))) {
                int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
                SettingsManager.saveData();
                QManager.testComplete(player, quest);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFarm(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String quest = QManager.getQuestId(player);
        if (!QManager.hasAvailable(player)) {
            if (!player.getLocation().getWorld().getName().equalsIgnoreCase("spawn")) {
                if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("farm") &&
                        event.getBlock().getType().equals(Material.valueOf(QManager.getMaterial(QManager.getQuestId(player)).toString()))) {
                    BlockData bdata = event.getBlock().getBlockData();
                    if (bdata instanceof Ageable) {
                        Ageable age = (Ageable) bdata;
                        if (age.getAge() == age.getMaximumAge()) {
                            int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                            SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
                            SettingsManager.saveData();
                            QManager.testComplete(player, quest);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        String quest = "";
        if (QManager.getQuest(player).equals("quest1")) {
            quest = QManager.getQuestOne(player);
        } else if (QManager.getQuest(player).equals("quest2")) {
            quest = QManager.getQuestTwo(player);
        } else if (QManager.getQuest(player).equals("quest3")) {
            quest = QManager.getQuestThree(player);
        }

        final String state = event.getState().name();
        if (event.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            Item fish = (Item) event.getCaught();
            ItemStack fishStack = fish.getItemStack();
            if (state.equalsIgnoreCase("CAUGHT_FISH") && QManager.getObjectiveType(quest).equals("fish") && fishStack.getType().equals(Material.valueOf(QManager.getType(QManager.getQuestId(player)).toUpperCase()))) {
                int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
                SettingsManager.saveData();
                QManager.testComplete(player, quest);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player) &&
                event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            String quest = "";
            if (QManager.getQuest(player).equals("quest1")) {
                quest = QManager.getQuestOne(player);
            } else if (QManager.getQuest(player).equals("quest2")) {
                quest = QManager.getQuestTwo(player);
            } else if (QManager.getQuest(player).equals("quest3")) {
                quest = QManager.getQuestThree(player);
            }
            if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("kill") &&
                    event.getEntityType().toString().equals(QManager.getType(QManager.getQuestId(player)).toUpperCase())) {
                int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
                SettingsManager.saveData();
                QManager.testComplete(player, quest);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCraft(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        String quest = "";
        if (QManager.getQuest(player).equals("quest1")) {
            quest = QManager.getQuestOne(player);
        } else if (QManager.getQuest(player).equals("quest2")) {
            quest = QManager.getQuestTwo(player);
        } else if (QManager.getQuest(player).equals("quest3")) {
            quest = QManager.getQuestThree(player);
        }
        if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("craft") &&
                event.getCurrentItem().getType().equals(Material.valueOf(QManager.getType(QManager.getQuestId(player)).toUpperCase())))
            if (event.getCurrentItem().getType().equals(Material.LEGACY_WOOL)) {
                if (event.isShiftClick()) {
                    ItemStack wool = new ItemStack(Material.LEGACY_WOOL, 1, (short) 0);
                    if (event.getCurrentItem().equals(wool)) {
                        int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                        int itemsChecked = 0;
                        int possibleCreations = 1;
                        byte b;
                        int i;
                        ItemStack[] arrayOfItemStack;
                        for (i = (arrayOfItemStack = event.getInventory().getMatrix()).length, b = 0; b < i; ) {
                            ItemStack item = arrayOfItemStack[b];
                            if (item != null && !item.getType().equals(Material.AIR)) {
                                System.out.println(item);
                                if (itemsChecked == 0) {
                                    possibleCreations = item.getAmount();
                                } else {
                                    possibleCreations = Math.min(possibleCreations, item.getAmount());
                                    itemsChecked++;
                                }
                            }
                            b++;
                        }
                        int amountOfItems = event.getRecipe().getResult().getAmount() * possibleCreations;
                        SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + amountOfItems));
                        SettingsManager.saveData();
                        QManager.testComplete(player, quest);
                    }
                } else if (event.getCursor().getType() == null || event.getCursor().getType().equals(Material.AIR)) {
                    int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                    int amount = event.getCurrentItem().getAmount();
                    SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + amount));
                    SettingsManager.saveData();
                    QManager.testComplete(player, quest);
                } else {
                    event.setCancelled(true);
                }
            } else if (event.isShiftClick()) {
                int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                int itemsChecked = 0;
                int possibleCreations = 1;
                byte b;
                int i;
                ItemStack[] arrayOfItemStack;
                for (i = (arrayOfItemStack = event.getInventory().getMatrix()).length, b = 0; b < i; ) {
                    ItemStack item = arrayOfItemStack[b];
                    if (item != null && !item.getType().equals(Material.AIR)) {
                        System.out.println(item);
                        if (itemsChecked == 0) {
                            possibleCreations = item.getAmount();
                        } else {
                            possibleCreations = Math.min(possibleCreations, item.getAmount());
                            itemsChecked++;
                        }
                    }
                    b++;
                }
                int amountOfItems = event.getRecipe().getResult().getAmount() * possibleCreations;
                SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + amountOfItems));
                SettingsManager.saveData();
                QManager.testComplete(player, quest);
            } else if (event.getCursor().getType() == null || event.getCursor().getType().equals(Material.AIR)) {
                int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                int amount = event.getCurrentItem().getAmount();
                SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + amount));
                SettingsManager.saveData();
                QManager.testComplete(player, quest);
            } else {
                event.setCancelled(true);
            }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEnchant(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        String quest = "";
        if (QManager.getQuest(player).equals("quest1")) {
            quest = QManager.getQuestOne(player);
        } else if (QManager.getQuest(player).equals("quest2")) {
            quest = QManager.getQuestTwo(player);
        } else if (QManager.getQuest(player).equals("quest3")) {
            quest = QManager.getQuestThree(player);
        }
        if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("enchant") && (
                QManager.getType(QManager.getQuestId(player)).split(";")[0].contains("any") || event.getItem().getType().equals(Material.valueOf(QManager.getType(QManager.getQuestId(player)).split(";")[0].toUpperCase()))) &&
                event.getEnchantsToAdd().containsKey(Enchantment.getByName(QManager.getType(QManager.getQuestId(player)).split(";")[1].toUpperCase())) && event.getEnchantsToAdd().get(Enchantment.getByName(QManager.getType(QManager.getQuestId(player)).split(";")[1].toUpperCase())).equals(Integer.valueOf(Integer.parseInt(QManager.getType(QManager.getQuestId(player)).split(";")[2])))) {
            int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
            SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
            SettingsManager.saveData();
            QManager.testComplete(player, quest);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String quest = "";
        if (QManager.getQuest(player).equals("quest1")) {
            quest = QManager.getQuestOne(player);
        } else if (QManager.getQuest(player).equals("quest2")) {
            quest = QManager.getQuestTwo(player);
        } else if (QManager.getQuest(player).equals("quest3")) {
            quest = QManager.getQuestThree(player);
        }
        if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("command") &&
                event.getMessage().contains("test"))
            Bukkit.broadcastMessage("test");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPressurePlate(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String quest = "";
        if (QManager.getQuest(player).equals("quest1")) {
            quest = QManager.getQuestOne(player);
        } else if (QManager.getQuest(player).equals("quest2")) {
            quest = QManager.getQuestTwo(player);
        } else if (QManager.getQuest(player).equals("quest3")) {
            quest = QManager.getQuestThree(player);
        }
        if (event.getAction().equals(Action.PHYSICAL) &&
                event.getClickedBlock().getType().equals(Material.STONE_PRESSURE_PLATE) &&
                QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("challenge")) {
            String world = SettingsManager.getConfig().getString("quests." + quest + ".type").split(";")[0];
            int x = Integer.parseInt(SettingsManager.getConfig().getString("quests." + quest + ".type").split(";")[1]);
            int y = Integer.parseInt(SettingsManager.getConfig().getString("quests." + quest + ".type").split(";")[2]);
            int z = Integer.parseInt(SettingsManager.getConfig().getString("quests." + quest + ".type").split(";")[3]);
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            if (event.getClickedBlock().getLocation().equals(loc)) {
                int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
                SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
                SettingsManager.saveData();
                QManager.testComplete(player, quest);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void shearEvent(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        String quest = "";
        if (QManager.getQuest(player).equals("quest1")) {
            quest = QManager.getQuestOne(player);
        } else if (QManager.getQuest(player).equals("quest2")) {
            quest = QManager.getQuestTwo(player);
        } else if (QManager.getQuest(player).equals("quest3")) {
            quest = QManager.getQuestThree(player);
        }
        if (QManager.hasQuest(player) && QManager.getObjectiveType(quest).equals("shear") &&
                event.getEntity().getType().equals(EntityType.valueOf(QManager.getType(QManager.getQuestId(player)).toUpperCase()))) {
            int current = SettingsManager.getData().getInt(player.getUniqueId().toString() + ".selected-quest.amount");
            SettingsManager.getData().set(player.getUniqueId().toString() + ".selected-quest.amount", Integer.valueOf(current + 1));
            SettingsManager.saveData();
            QManager.testComplete(player, quest);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        (new BukkitRunnable() {
            public void run() {
                if (QManager.hasAvailable(player)) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 2.0F, 2.0F);
                    player.sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&6Tasks: &eHey! I noticed you don't have a task selected, you should go check out &6/tasks &eand see if you have any tasks available!"));
                }
            }
        }).runTaskLater(Main.plugin, 200L);
    }
}


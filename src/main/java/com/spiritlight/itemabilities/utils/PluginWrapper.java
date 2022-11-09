package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Level;

/**
 * A complete class containing various static methods to help
 * better access to the plugin.
 *
 */
public class PluginWrapper {
    private PluginWrapper() {}

    // Using this because apparently the implement will NOT work for weird reasons.
    public static boolean containsEnchantment(ItemMeta meta, Enchantment enchantment) {
        if(meta == null) {
            ItemAbilities.logger.log(Level.WARNING, "null passed to containsEnchantment");
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for(StackTraceElement element : stackTraceElements) {
                ItemAbilities.logger.log(Level.INFO, "\tat " + element.toString());
            }
            return false;
        }
         return meta.hasEnchant(enchantment);
    }

    public static long toTick(long time, TimeUnit unit) {
        return switch (unit) {
            case DAYS -> 1728000 * time;
            case HOURS -> 72000 * time;
            case MINUTES -> 1200 * time;
            case SECONDS -> 20 * time;
            case MILLISECONDS -> time / 50;
            case MICROSECONDS -> time / 50000;
            case NANOSECONDS -> time / 50000000;
        };
    }

    public static NamespacedKey newNameSpacedKey(String key) {
        return new NamespacedKey(ItemAbilities.INSTANCE, key);
    }

    public static int scheduleTask(Runnable exec, long delayTick) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(ItemAbilities.INSTANCE, exec, delayTick);
    }

    /**
     * @see org.bukkit.scheduler.BukkitScheduler#scheduleSyncRepeatingTask(Plugin, Runnable, long, long)
     */
    public static int scheduleRepeatTask(Runnable exec, long delayTick, long periodTick) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(ItemAbilities.INSTANCE, exec, delayTick, periodTick);
    }

    public static void terminateTask(int taskID) {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    /**
     *
     * @param key Namespace key for this ability
     * @param name The internal name for this ability
     * @param abilityName The displayed name for this ability
     * @param abilityDescription The description displayed for this ability
     * @param abilityVisible Whether this ability should be visible on enchant
     * @param maxLevel The maximum level
     * @param target Targets this ability can be applied to
     * @param cursed Whether this ability is cursed
     * @return Generated ability
     */
    public static Ability newAbility(NamespacedKey key, String name, String abilityName, String[] abilityDescription,
                                     boolean abilityVisible, int maxLevel,
                                     EnchantmentTarget target, boolean cursed) {
        return new Ability(key, name) {
            @Override
            public String[] getAbilityDescription() {
                return abilityDescription;
            }

            @Override
            public boolean abilityVisible() {
                return abilityVisible;
            }

            @Override
            public @NotNull String getAbilityName() {
                return abilityName;
            }

            @NotNull
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getMaxLevel() {
                return maxLevel;
            }

            @NotNull
            @Override
            public EnchantmentTarget getItemTarget() {
                return target;
            }

            @Override
            public boolean isCursed() {
                return cursed;
            }

            @Override
            public boolean conflictsWith(@NotNull Enchantment other) {
                return false;
            }

            @Override
            public boolean canEnchantItem(@NotNull ItemStack item) {
                return true;
            }
        };
    }

    public static Enchantment newEnchant(NamespacedKey key, String name, int minLevel, int maxLevel,
                                         EnchantmentTarget target, boolean treasure, boolean cursed,
                                         Predicate<Enchantment> conflicts, Predicate<ItemStack> canEnchant) {
        return new Enchantment(key) {
            @NotNull
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getMaxLevel() {
                return maxLevel;
            }

            @Override
            public int getStartLevel() {
                return minLevel;
            }

            @NotNull
            @Override
            public EnchantmentTarget getItemTarget() {
                return target;
            }

            @Override
            public boolean isTreasure() {
                return treasure;
            }

            @Override
            public boolean isCursed() {
                return cursed;
            }

            @Override
            public boolean conflictsWith(@NotNull Enchantment other) {
                return conflicts == null || conflicts.test(other);
            }

            @Override
            public boolean canEnchantItem(@NotNull ItemStack item) {
                return canEnchant == null ? getItemTarget().includes(item) : canEnchant.test(item);
            }
        };
    }

    public static void getCurrency(Player player, int amount) {
        getCurrency(player, amount, Currency.PLASMA, Currency.NORMAL);
    }

    public static void getCurrency(Player player, int amount, Currency maxType) {
        getCurrency(player, amount, maxType, Currency.NORMAL);
    }

    public static void getCurrency(Player sender, int amount, Currency maxType, Currency minType) {
        if(maxType.amount < minType.amount) throw new IllegalArgumentException("maxType cannot be smaller than minType!");
        Map<Currency, Integer> map = getTypeAndQuantity(amount, maxType, minType);
        for(Currency currency : map.keySet()) {
            if(map.get(currency).equals(0)) continue;
            ItemStack stack = getCurrencyItem(currency);
            stack.setAmount(map.get(currency));
            sender.getInventory().addItem(stack);
        }
    }

    public static ItemStack getCurrencyItem(Currency currency) {
        ItemStack stack = new ItemStack(currency.material);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + currency.toString());
        meta.setLore(List.of("",
                ChatColor.YELLOW + "Potato Economy",
                ChatColor.GOLD + "Worth " + ChatColor.GREEN + currency.amount + ChatColor.GOLD + " Potato" + (currency == Currency.NORMAL ? "" : "es")
        ));
        stack.setItemMeta(meta);
        return stack;
    }

    private static Map<Currency, Integer> getTypeAndQuantity(int parse, Currency maxType, Currency minType) {
        Map<Currency, Integer> ret = new HashMap<>();
        List<Currency> currencyList = new ArrayList<>(Arrays.stream(Currency.values()).toList());
        currencyList.removeIf(currency -> currency.amount > maxType.amount || currency.amount < minType.amount);
        for(Currency currency : currencyList.stream().sorted(
                (a, b) -> b.amount - a.amount
        ).toList()) {
            ret.put(currency, parse / currency.amount);
            parse %= currency.amount;
        }
        return ret;
    }

    public enum Currency {
        NORMAL(1, Material.POTATO), // 1, next = 8
        ROD(8, Material.BLAZE_ROD), // 8, next = 8
        BUNDLE(64, Material.BOOK), // 64, next = 16
        CONDENSED(1024, Material.RAW_GOLD), // 1024, next = 4
        POWDERED(4096, Material.GLOWSTONE_DUST), // 4096, next = 4
        LIQUID(16384, Material.HONEY_BOTTLE), // 16384, next = 4
        STACK(65536, Material.COCOA_BEANS), // 65536, next = 4
        PLASMA(262144, Material.DRAGON_BREATH); // 262144, next = null

        private final int amount;
        private final Material material;

        Currency(int amount, Material material) {
            this.amount = amount;
            this.material = material;
        }

        @Nullable(/* if string is not created via Currency.toString() or contain the corresponding name */)
        public static Currency fromName(String string) {
            if(string == null) return null;
            for(Currency c : values()) {
                if(string.contains(c.toString()))
                    return c;
            }
            return null;
        }

        @Nullable(/* if ItemStack have no ItemMeta or fromName() returns false */)
        public static Currency fromItemStack(ItemStack itemStack) {
            if(itemStack == null || itemStack.getItemMeta() == null) return null;
            return fromName(itemStack.getItemMeta().getDisplayName());
        }

        /**
         * If this method returns true, fromName() will return a non-null value.
         * @param itemStack ItemStack to test
         * @return true if it is a valid currency.
         */
        public static boolean isCurrency(ItemStack itemStack) {
            if(itemStack == null) return false;
            if(!itemStack.hasItemMeta()) return false;
            if(!PluginWrapper.containsEnchantment(itemStack.getItemMeta(), EnchantmentUtils.CURRENCY)) return false;
            PluginWrapper.Currency currency = PluginWrapper.Currency.fromName(itemStack.getItemMeta().getDisplayName());
            return currency != null;
        }

        public int getAmount() {
            return amount;
        }

        public Material getMaterial() {
            return material;
        }

        @Nullable(/* If the currency is PLASMA */)
        public Currency getNext() {
            return switch(this) {
                case PLASMA -> null;
                case STACK -> PLASMA;
                case LIQUID -> STACK;
                case POWDERED -> LIQUID;
                case CONDENSED -> POWDERED;
                case BUNDLE -> CONDENSED;
                case ROD -> BUNDLE;
                case NORMAL -> ROD;
            };
        }

        @Nullable(/* If the currency is NORMAL */)
        public Currency getPrevious() {
            return switch(this) {
                case PLASMA -> STACK;
                case STACK -> LIQUID;
                case LIQUID -> POWDERED;
                case POWDERED -> CONDENSED;
                case CONDENSED -> BUNDLE;
                case BUNDLE -> ROD;
                case ROD -> NORMAL;
                case NORMAL -> null;
            };
        }

        @Override
        public String toString() {
            return switch(this) {
                case PLASMA -> "Plasma Potato";
                case STACK -> "Stack of Potatoes";
                case LIQUID -> "Liquid Potato";
                case POWDERED -> "Powdered Potato";
                case CONDENSED -> "Condensed Potato";
                case BUNDLE -> "Potato Bundle";
                case ROD -> "Potato Rod";
                case NORMAL -> "Potato";
            };
        }
    }
}

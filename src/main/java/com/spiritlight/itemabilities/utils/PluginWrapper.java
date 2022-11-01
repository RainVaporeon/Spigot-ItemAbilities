package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import org.bukkit.Bukkit;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * A complete class containing various static methods to help
 * better access to the plugin.
 *
 */
public class PluginWrapper {
    private PluginWrapper() {}

    // Using this because apparently the implement will NOT work for weird reasons.
    public static boolean containsEnchantment(ItemStack stack, Enchantment enchantment) {
        if(stack == null || enchantment == null) return false;
        ItemStack stx = stack.clone();
        return stx.removeEnchantment(enchantment) != 0;
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
     * @param conflicts Conflicts enchantment, null to always return false
     * @param canEnchant Can enchant on this itemstack, null to test with target.
     * @return Generated ability
     */
    public static Ability newAbility(NamespacedKey key, String name, String abilityName, String[] abilityDescription,
                                     boolean abilityVisible, int maxLevel,
                                     EnchantmentTarget target, boolean cursed,
                                     Predicate<Enchantment> conflicts, Predicate<ItemStack> canEnchant) {
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
                return conflicts != null && conflicts.test(other);
            }

            @Override
            public boolean canEnchantItem(@NotNull ItemStack item) {
                return canEnchant == null ? getItemTarget().includes(item) : canEnchant.test(item);
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

    public static void getCurrency(Player sender, int amount) {
        Map<Currency, Integer> map = getTypeAndQuantity(amount);
        for(Currency currency : map.keySet()) {
            ItemStack stack = new ItemStack(currency.material);
            ItemMeta meta = new SpiritItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(EnchantmentUtils.CURRENCY, 1, true);
            stack.setItemMeta(meta);
            sender.getInventory().addItem(stack);
        }
    }

    private static Map<Currency, Integer> getTypeAndQuantity(int parse) {
        Map<Currency, Integer> ret = new HashMap<>();
        for(Currency currency : Arrays.stream(Currency.values()).sorted(
                Comparator.comparingInt(a -> a.amount)
        ).toList()) {
            ret.put(currency, parse / currency.amount);
            parse %= currency.amount;
        }
        return ret;
    }

    public enum Currency {
        NORMAL(1, Material.POTATO), // 1
        BLOCK(9, Material.HAY_BLOCK), // 9
        BUNDLE(64, Material.BOOK), // 64
        CONDENSED(1024, Material.GOLD_BLOCK), // 1024
        POWDERED(4096, Material.GLOWSTONE_DUST), // 4096
        LIQUID(16384, Material.HONEY_BOTTLE), // 16384
        STACK(65536, Material.BUNDLE), // 65536
        PLASMA(262144, Material.DRAGON_BREATH); // 262144

        public final int amount;
        public final Material material;

        Currency(int amount, Material material) {
            this.amount = amount;
            this.material = material;
        }

        public int getAmount() {
            return amount;
        }

        public Material getMaterial() {
            return material;
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
                case BLOCK -> "Potato Block";
                case NORMAL -> "Potato";
            };
        }
    }
}

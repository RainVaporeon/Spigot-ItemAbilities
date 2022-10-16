package com.spiritlight.itemabilities.utils;

import com.google.common.collect.Multimap;
import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * A complete class containing various static methods to help
 * better access to the plugin.
 *
 */
public class PluginWrapper {
    private PluginWrapper() {}

    public static final String FORMAT = "\u00A7";
    
    public static void subscribeEvents(Listener listener) {
        ItemAbilities.INSTANCE.getServer().getPluginManager().registerEvents(listener, ItemAbilities.INSTANCE);
    }

    @Nullable
    public static Plugin getPluginInstance(String plugin) {
        return Bukkit.getPluginManager().getPlugin(plugin);
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

    @CheckReturnValue
    public static int scheduleTask(Runnable exec, long delayTick) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(ItemAbilities.INSTANCE, exec, delayTick);
    }

    /**
     * @see org.bukkit.scheduler.BukkitScheduler#scheduleSyncRepeatingTask(Plugin, Runnable, long, long) 
     */
    @CheckReturnValue
    public static int scheduleRepeatTask(Runnable exec, long delayTick, long periodTick) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(ItemAbilities.INSTANCE, exec, delayTick, periodTick);
    }

    public static void terminateTask(int taskID) {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static Ability newAbility(NamespacedKey key, String name, String abilityName, String abilityDescription,
                                     boolean abilityVisible, int maxLevel,
                                     EnchantmentTarget target, boolean cursed,
                                     Predicate<Enchantment> conflicts, Predicate<ItemStack> canEnchant) {
        return new Ability(key, name) {
            @Override
            public @NotNull String getAbilityDescription() {
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
                return conflicts == null || conflicts.test(other);
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
}

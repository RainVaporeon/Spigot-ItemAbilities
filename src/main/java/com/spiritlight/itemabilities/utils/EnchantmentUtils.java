package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.ItemAbilities;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnchantmentUtils {
    public static final Enchantment CURRENCY = PluginWrapper.newAbility(
            ItemAbilities.CURRENCY, "currency", "Currency",
            new String[0], false, 0, EnchantmentTarget.ALL,
            false
    );

    public static int getEnchantmentLevel(ItemStack itemStack, Enchantment enchantment) {
        return itemStack.getItemMeta().getEnchantLevel(enchantment);
    }

    public static boolean entityHasEnchantment(LivingEntity entity, Enchantment enchantment) {
        return entityHasEnchantment(entity, enchantment, null);
    }

    public static boolean hasEnchant(ItemStack stack, Enchantment enchantment) {
        return PluginWrapper.containsEnchantment(stack.getItemMeta(), enchantment);
    }

    public static boolean entityHasEnchantment(LivingEntity entity, Enchantment enchantment, EnchantmentTarget target) {
        final EntityEquipment equipment = entity.getEquipment();
        if(equipment == null) return false;
        ItemStack[] armorContents = equipment.getArmorContents();
        ItemStack[] handItems = new ItemStack[] {equipment.getItemInMainHand(), equipment.getItemInOffHand()};
        for(ItemStack i : armorContents) {
            if(i == null) continue;
            if(target != null && !target.includes(i)) continue;
            if(!i.hasItemMeta()) continue;
            if(PluginWrapper.containsEnchantment(i.getItemMeta(), enchantment)) return true;
        }
        for(ItemStack i : handItems) {
            if(i == null) continue;
            if(target != null && !target.includes(i)) continue;
            if(!i.hasItemMeta()) continue;
            if(PluginWrapper.containsEnchantment(i.getItemMeta(), enchantment)) return true;
        }
        return false;
    }

    public static List<ItemStack> getEnchantedItems(LivingEntity entity, Enchantment enchantment) {
        return getEnchantedItems(entity, enchantment, null);
    }

    public static List<ItemStack> getEnchantedItems(LivingEntity entity, Enchantment enchantment, EnchantmentTarget target) {
        if(entity == null || enchantment == null || target == null) return Collections.emptyList();
        final List<ItemStack> ret = new ArrayList<>();
        boolean hasEquipments = entity.getEquipment() != null;
        if(!hasEquipments) return Collections.emptyList();
        final EntityEquipment equipment = entity.getEquipment();
        ItemStack[] armorContents = equipment.getArmorContents();
        ItemStack[] handItems = new ItemStack[] {equipment.getItemInMainHand(), equipment.getItemInOffHand()};
        for(ItemStack i : armorContents) {
            if(i == null) continue;
            if(!target.includes(i)) continue;
            if(!i.hasItemMeta()) continue;
            if(PluginWrapper.containsEnchantment(i.getItemMeta(), enchantment)) ret.add(i);
        }
        for(ItemStack i : handItems) {
            if(i == null) continue;
            if(!target.includes(i)) continue;
            if(!i.hasItemMeta()) continue;
            if(PluginWrapper.containsEnchantment(i.getItemMeta(), enchantment)) ret.add(i);
        }
        return ret;
    }
}

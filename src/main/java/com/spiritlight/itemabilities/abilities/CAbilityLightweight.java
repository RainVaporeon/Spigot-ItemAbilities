package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class CAbilityLightweight implements Listener {
    public static final Ability ability = Attributes.LIGHTWEIGHT;

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) return;
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if(EnchantmentUtils.entityHasEnchantment((LivingEntity) event.getEntity(), ability)) {
            double modifier = 1.0D;
            for(ItemStack i : EnchantmentUtils.getEnchantedItems((LivingEntity) event.getEntity(), ability)) {
                float mod = 1 - (EnchantmentUtils.getEnchantmentLevel(i, ability) / 100.0F);
                ItemAbilities.logger.log(Level.INFO, "Reduction: Reduce " + EnchantmentUtils.getEnchantmentLevel(i, ability) + "% fall damage");
                modifier *= mod;
            }
            ItemAbilities.logger.log(Level.INFO, "Final reduction modifier: " + (1 - modifier));
            if(modifier <= 0) {
                ItemAbilities.logger.log(Level.INFO, "Negated fall damage");
                event.setCancelled(true);
                return;
            }
            event.setDamage(event.getDamage() * modifier);
        }
    }
}

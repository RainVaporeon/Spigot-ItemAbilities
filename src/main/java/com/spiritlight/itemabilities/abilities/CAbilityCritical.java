package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.Probabilities;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class CAbilityCritical implements Listener {
    public static final Ability ability = Attributes.CRITICAL_CHANCE;

    @EventHandler
    public void onFallDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity receiver)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (attacker.getEquipment() == null) return;
        int rate = 0;
        for (ItemStack i : EnchantmentUtils.getEnchantedItems((LivingEntity) event.getEntity(), ability)) {
            // Probability of this trigger
            rate += EnchantmentUtils.getEnchantmentLevel(i, ability);
        }
        if (Probabilities.passPredicate(rate)) {
            World world = receiver.getWorld();
            world.spawnParticle(Particle.CRIT_MAGIC, receiver.getLocation(), 35);
            world.playSound(receiver.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
            event.setDamage(event.getDamage() * 1.5F);
        }
    }
}

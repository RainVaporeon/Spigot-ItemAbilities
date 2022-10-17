package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.*;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class CAbilityStrikeBack implements Listener {
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_STRIKEBACK, "strikeback", "Revenge",
            new String[] {"35% chance to hit back at 50% damage received", ChatColor.GRAY + "(Unstackable)"},
            true, 1, EnchantmentTarget.BREAKABLE, false, null, null
    );

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onAttacked(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity attacker)) return;
        if(!(event.getEntity() instanceof LivingEntity receiver)) return;
        if(EnchantmentUtils.entityHasEnchantment(receiver, ability)) {
            boolean b = RANDOM.nextInt(100) > 64; // 1 ~ 100
            if(!b) return;
            World world = attacker.getWorld();
            double damage = event.getDamage() * 0.5;
            PluginWrapper.scheduleTask(() -> {
                try {
                    world.spawnParticle(Particle.CRIT_MAGIC, attacker.getLocation(), 25);
                    world.playSound(attacker.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1.5F, 1.0F);
                    attacker.setHealth(attacker.getHealth() - damage);
                    attacker.setLastDamageCause(new EntityDamageEvent(attacker, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));
                    attacker.setLastDamage(damage);
                    attacker.setNoDamageTicks(0);
                } catch (IllegalArgumentException ignored) { /* Bukkit giving away exceptions for free */}
            }, 10);
        }
    }
}

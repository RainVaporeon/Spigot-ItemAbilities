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

import java.util.Random;

public class CAbilityDoubleStrike implements Listener {
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_DOUBLESTRIKE, "double_strike", "DoubleStrike",
            new String[] {"20% chance to attack again", ChatColor.GRAY + "(Unstackable)"},
            true, 1, EnchantmentTarget.WEAPON, false, null, null
    );

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity attacker)) return;
        if(!(event.getEntity() instanceof LivingEntity receiver)) return;
        if(EnchantmentUtils.entityHasEnchantment(attacker, ability)) {
            boolean b = RANDOM.nextInt(100) > 79; // 1 ~ 100
            if(!b) return;
            World world = attacker.getWorld();
            PluginWrapper.scheduleTask(() -> {
                try {
                    Location loc = attacker.getLocation().subtract(receiver.getLocation());
                    world.spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
                    world.playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
                    receiver.setNoDamageTicks(0);
                    attacker.attack(receiver);
                    receiver.setNoDamageTicks(0);
                } catch (IllegalArgumentException ignored) { /* Bukkit giving away exceptions for free */}
            }, 20);
        }
    }
}

package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import com.spiritlight.itemabilities.utils.Probabilities;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class CAbilityStrikeBack implements Listener {
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_STRIKEBACK, "strikeback", "Revenge",
            new String[] {"35% chance to return a hit", ChatColor.GRAY + "(Unstackable)"},
            true, 1, EnchantmentTarget.BREAKABLE, false
    );

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onAttacked(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof LivingEntity attacker)) return;
        if(!(event.getEntity() instanceof LivingEntity receiver)) return;
        if(EnchantmentUtils.entityHasEnchantment(receiver, ability)) {
            if(!Probabilities.passPredicate(65)) return;
            World world = attacker.getWorld();
            PluginWrapper.scheduleTask(() -> {
                if(receiver.isDead()) return;
                try {
                    world.spawnParticle(Particle.CRIT_MAGIC, attacker.getLocation(), 25);
                    world.playSound(attacker.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1.5F, 1.0F);
                    attacker.setNoDamageTicks(0);
                    receiver.attack(attacker);
                    attacker.setNoDamageTicks(0);
                } catch (IllegalArgumentException ignored) { /* Bukkit giving away exceptions for free */}
            }, 10);
        }
    }
}

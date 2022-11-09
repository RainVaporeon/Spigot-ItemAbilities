package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CAbilityFlamingHit implements Listener {
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_FLAMING_HIT, "flaming", "Flame",
            new String[] {"Entity hit by this ability has",
            "a 50% chance to be lit on fire,",
            "lasting 5 seconds."}, true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    private static final Random RANDOM = new Random();

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof LivingEntity attacker)) return;
        if(!(e.getEntity() instanceof LivingEntity receiver)) return;
        if(attacker.getEquipment() == null) return;
        ItemStack stx = attacker.getEquipment().getItemInMainHand();
        if(!stx.hasItemMeta()) return;
        World world = e.getEntity().getWorld();
        if(EnchantmentUtils.hasEnchant(stx, ability)) {
            if(RANDOM.nextBoolean()) {
                world.spawnParticle(Particle.FLAME, receiver.getLocation(), 50);
                world.playSound(receiver.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
                receiver.setFireTicks(100);
            }
        }
    }
}

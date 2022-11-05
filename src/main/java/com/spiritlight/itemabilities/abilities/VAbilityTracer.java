package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.EntityEffect;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Combat ability: Tracer<br>
 * <br>
 * Arrows shot has tracking ability.
 */
public class VAbilityTracer implements Listener {
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_TRACER, "tracer", "Tracer",
            new String[]{"Arrows shot has tracking ability."},
            true, 1, EnchantmentTarget.BOW, false
    );

    @EventHandler
    public void onBowShot(@NotNull EntityShootBowEvent event) {
        if (event.getBow() == null) return;
        if(!event.getBow().hasItemMeta()) return;
        if (!PluginWrapper.containsEnchantment(event.getBow().getItemMeta(), ability))
            return;
        final Entity projectile = event.getProjectile();
        final int task = PluginWrapper.scheduleRepeatTask(() -> {
            try {
                projectile.playEffect(EntityEffect.FIREWORK_EXPLODE);
                if(projectile.getTicksLived() >= 8) {
                    List<Entity> entityList = projectile.getNearbyEntities(3.5, 3, 3.5);
                    for(Entity entity : entityList) {
                        if(!(entity instanceof LivingEntity)) continue;
                        if(((LivingEntity) entity).getPotionEffect(PotionEffectType.GLOWING) != null) continue;
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, (int) PluginWrapper.toTick(8, TimeUnit.SECONDS), 1));
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), t);
            }
        }, 3, 81);
    }
}

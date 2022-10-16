package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.EventListener;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Visual ability: Tracer<br>
 * <br>
 * Arrows shot has tracking ability. Causes nearby (4 Blocks) enemies
 * from shot arrow to glow after it's shot for at least 0.5 seconds.
 */
public class VAbilityTracer extends EventListener {
    public static final Ability ability = new Ability(ItemAbilities.ABILITY_TRACER, "tracer") {
        @Override
        public @NotNull String getAbilityDescription() {
            return """
                    Arrows shot has tracking ability.

                    Causes nearby enemies from shot arrow to glow
                    after it's shot for at least 0.5 seconds.""";
        }

        @Override
        public boolean abilityVisible() {
            return false;
        }

        @NotNull
        @Override
        public String getName() {
            return ability.getKey().getKey();
        }

        @Override
        public int getMaxLevel() {
            return 0;
        }

        @NotNull
        @Override
        public EnchantmentTarget getItemTarget() {
            return EnchantmentTarget.BOW;
        }

        @Override
        public boolean isCursed() {
            return false;
        }

        @Override
        public boolean conflictsWith(@NotNull Enchantment other) {
            return false;
        }

        @Override
        public boolean canEnchantItem(@NotNull ItemStack item) {
            return getItemTarget().includes(item);
        }
    };

    @EventHandler
    public void onBowShot(@NotNull EntityShootBowEvent event) {
        if (event.getBow() == null) return;
        if (!event.getBow().hasItemMeta()) return;
        if(!event.getBow().getItemMeta().hasLore()) return;
        // Detect whether this item actually qualifies
        boolean flag = event.getBow().getItemMeta().getLore().stream().anyMatch(lore -> lore.equalsIgnoreCase(ability.getAbilityName()));
        if(!flag) return;
        final Entity entity = event.getProjectile();
        final int task = PluginWrapper.scheduleRepeatTask(() -> {
            Arrow arrow = (Arrow) entity;
            if(arrow.isOnGround()) return;
            if(arrow.getTicksLived() >= 10) {
                List<Entity> entityList = arrow.getNearbyEntities(4, 4, 4);
                for(Entity e : entityList) {
                    if(!(e instanceof LivingEntity)) continue;
                    if(((LivingEntity) e).hasPotionEffect(PotionEffectType.GLOWING)) continue;
                    ((LivingEntity) e).addPotionEffect(
                            new PotionEffect(PotionEffectType.GLOWING, (int) PluginWrapper.toTick(8, TimeUnit.SECONDS), 0)
                    );
                }
            }
        }, 10, 100);
    }
}

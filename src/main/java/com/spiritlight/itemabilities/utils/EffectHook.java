package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Attributes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EffectHook {
    private static final List<Hook> effectHooks = List.of(
            new Hook(PotionEffectType.JUMP, 100, 0, Attributes.JUMP_BOOST, true)
    );

    public EffectHook() {}

    public static void run() {
        for(World world : Bukkit.getWorlds()) {
            List<LivingEntity> entities = world.getLivingEntities();
            for(LivingEntity entity : entities) {
                for(Hook hook : effectHooks) {
                    boolean flag = hook.isScaleByLevel();
                    List<ItemStack> effects = EnchantmentUtils.getEnchantedItems(entity, hook.getRequiredAbility());
                    if(effects.isEmpty()) continue;
                    Effect e = new Effect(hook.getEffect());
                    if(flag) {
                        int level = 0;
                        for(ItemStack i : effects) {
                            level += i.getEnchantmentLevel(hook.getRequiredAbility());
                        }
                        e.setAmplifier(level);
                    }
                    entity.addPotionEffect(e.create());
                }
            }
        }
    }


    private static class Hook {
        private final Effect effect;
        private final Ability requiredAbility;
        private final boolean scaleByLevel;

        public Hook(Effect effect, Ability requiredAbility, boolean scaleByLevel) {
            this.effect = effect;
            this.requiredAbility = requiredAbility;
            this.scaleByLevel = scaleByLevel;
        }

        public Hook(PotionEffectType type, int duration, int amplifier, Ability requiredAbility, boolean scaleByLevel) {
            this.effect = new Effect(type, duration, amplifier);
            this.requiredAbility = requiredAbility;
            this.scaleByLevel = scaleByLevel;
        }

        public Effect getEffect() {
            return effect;
        }

        public Ability getRequiredAbility() {
            return requiredAbility;
        }

        public boolean isScaleByLevel() {
            return scaleByLevel;
        }
    }
}

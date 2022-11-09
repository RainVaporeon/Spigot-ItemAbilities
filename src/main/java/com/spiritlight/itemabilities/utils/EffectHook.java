package com.spiritlight.itemabilities.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EffectHook implements Runnable {
    @Override
    public void run() {
        try {
            for(World world : Bukkit.getWorlds()) {
                List<LivingEntity> entities = world.getLivingEntities();
                for(LivingEntity entity : entities) {
                    for(EffectHooks.Hook hook : EffectHooks.getEffectHooks()) {
                        boolean flag = hook.isScaleByLevel();
                        List<ItemStack> effects = EnchantmentUtils.getEnchantedItems(entity, hook.getRequiredAbility());
                        if(effects.isEmpty()) continue;
                        Effect e = new Effect(hook.getEffect());
                        if(flag) {
                            int level = 0;
                            for(ItemStack i : effects) {
                                level += i.getEnchantmentLevel(hook.getRequiredAbility());
                            }
                            e.setAmplifier(Math.min(255, Math.max(0, level)));
                        }
                        entity.addPotionEffect(e.create());
                    }
                }
            }
        } catch (Throwable t) {
            if(t instanceof ThreadDeath) throw t;
            t.printStackTrace();
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), t);
        }
    }



}

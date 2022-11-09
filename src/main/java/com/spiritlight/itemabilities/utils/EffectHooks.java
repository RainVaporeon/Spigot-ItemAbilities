package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Attributes;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EffectHooks {
    private static final List<Hook> effectHooks = List.of(
            new Hook(PotionEffectType.JUMP, 100, 0, Attributes.JUMP_BOOST, true)
    );

    public static List<Hook> getEffectHooks() {
        return effectHooks;
    }

    public static class Hook {
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

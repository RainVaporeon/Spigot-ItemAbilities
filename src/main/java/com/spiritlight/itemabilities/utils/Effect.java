package com.spiritlight.itemabilities.utils;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Effect {
    private int amplifier;
    private int duration;
    private PotionEffectType type;

    public Effect(PotionEffectType type, int duration, int amplifier) {
        this.amplifier = amplifier;
        this.duration = duration;
        this.type = type;
    }

    public Effect(Effect e) {
        this.amplifier = e.amplifier;
        this.duration = e.duration;
        this.type = e.type;
    }

    public PotionEffect create() {
        return new PotionEffect(type, duration, amplifier);
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public PotionEffectType getType() {
        return type;
    }

    public void setType(PotionEffectType type) {
        this.type = type;
    }
}

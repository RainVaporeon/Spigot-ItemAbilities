package com.spiritlight.itemabilities.abilities;

import org.apache.commons.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum AttackSpeed {
    /*
    Typing: Wood / Stone / Gold / Iron / Diamond / Netherite
    Sword: Universally 1.6 Attack Speed
    Shovel: Universally 1 Attack Speed
    Pickaxe: Universally 1.2 Attack Speed
    Axe: 0.8 / 0.8 / 1 / 0.9 / 1 / 1  Attack Speed
    Hoe:  1  /  2  / 1 /  3  / 4 / 4  Attack Speed
    Generic Items: 4 Attack Speed
     */
    ULTRA_FAST(4F - 4, "ULTRA_FAST"),
    SUPER_FAST(3F - 4, "SUPER_FAST"),
    VERY_FAST(2.6F - 4, "VERY_FAST"),
    FAST(2.2F - 4, "FAST"),
    NORMAL(1.6F - 4, "NORMAL"),
    SLOW(1 - 4, "SLOW"),
    VERY_SLOW(0.8F - 4, "VERY_SLOW"),
    SUPER_SLOW(0.5F - 4, "SUPER_SLOW");

    private final float modifier;
    private final String name;

    AttackSpeed(float modifier, String name) {
        this.modifier = modifier;
        this.name = name;
    }

    public float getModifier() {
        return modifier;
    }

    public String getName() {
        return name;
    }

    public String getFormattedName() {
        return ChatColor.GRAY + WordUtils.capitalizeFully(this.getName().replace("_", " ") + " Attack Speed");
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static List<String> getNames() {
        List<String> ret = new ArrayList<>();
        for(AttackSpeed speed : values()) {
            ret.add(speed.getName());
        }
        return ret;
    }

    @Nullable(/* if speed is invalid */)
    public static AttackSpeed fromName(String speed) {
        List<AttackSpeed> ret = Arrays.stream(values()).filter(attackSpeed -> attackSpeed.getName().equalsIgnoreCase(speed))
                .toList();
        if(ret.size() == 0) return null;
        return ret.get(0);
    }
}

package com.spiritlight.itemabilities.abilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Ability extends Enchantment {
    private final String name;


    public Ability(NamespacedKey key, String name) {
        super(key);
        this.name = name;
    }

    public abstract String[] getAbilityDescription();

    public abstract boolean abilityVisible();

    @NotNull
    public String getAbilityName() {
        return name;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public int getStartLevel() {
        return -32767;
    }

    @Override
    public int getMaxLevel() {
        return 32767;
    }

    public final List<String> descriptionList = new ArrayList<>() {{
        add("");
        add(ChatColor.GOLD + getAbilityName());
        for(String desc : getAbilityDescription()) {
            add(ChatColor.YELLOW + desc);
        }
    }};

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof Ability ability)) return false;
        return this.getKey().equals(ability.getKey());
    }
}

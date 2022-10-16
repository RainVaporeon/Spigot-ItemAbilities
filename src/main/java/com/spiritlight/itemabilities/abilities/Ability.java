package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Ability extends Enchantment {
    private String name;


    public Ability(NamespacedKey key, String name) {
        super(key);
    }

    @NotNull
    public abstract String getAbilityDescription();

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
        return 0;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return super.getKey();
    }

    public final List<String> descriptionList = new ArrayList<>() {{
        add("");
        add(PluginWrapper.FORMAT + "6" + getAbilityName() + ": " + PluginWrapper.FORMAT + "e" + getAbilityDescription());
    }};
}

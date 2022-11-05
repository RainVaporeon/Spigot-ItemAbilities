package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.Arrays;
import java.util.List;

public class Tags {
    public static final Ability IRREPAIRABLE = PluginWrapper.newAbility(
            ItemAbilities.CANNOT_REPAIR, "norepair", "NoRepair",
            new String[]{ChatColor.RED + "Cannot be repaired"}, false, 1, EnchantmentTarget.BREAKABLE, false
    );

    public static final Ability UNBREAKABLE = PluginWrapper.newAbility(
            ItemAbilities.UNBREAKABLE, "unbreakable", "Unbreakable",
            new String[]{ChatColor.BLUE + "Unbreakable"}, false, 1, EnchantmentTarget.BREAKABLE, false
    );

    public static final Ability[] abilities = new Ability[] {
            IRREPAIRABLE,
            UNBREAKABLE
    };

    public static final List<String> names = List.of(
            IRREPAIRABLE.getAbilityName(),
            UNBREAKABLE.getAbilityName()
    );

    public static Ability getTagByName(String name) {
        List<Ability> ret = Arrays.stream(abilities).filter(a -> a.getAbilityName().equalsIgnoreCase(name)).toList();
        if(ret.size() == 0) return null;
        return ret.get(0);
    }
}

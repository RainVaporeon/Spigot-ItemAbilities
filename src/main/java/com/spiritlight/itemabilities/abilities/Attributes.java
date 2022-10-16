package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.EnchantmentTarget;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Attributes {

    public static final Ability SPEED = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_SPEED, "speed", "% Movement Speed",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false, null, null
    );

    public static final Ability ATTACK_DAMAGE_PCT = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ATTACK_DAMAGE_PCT, "atkdmgpct", "% Attack Damage",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false, null, null
    );

    public static final Ability ATTACK_DAMAGE_RAW = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ATTACK_DAMAGE_RAW, "atkdmgraw", " Attack Damage",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false, null, null
    );

    public static final Ability LIGHTWEIGHT = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_LIGHTWEIGHT, "lightweight", "% Reduced Fall Damage",
            new String[0], true, 100, EnchantmentTarget.BREAKABLE,
            false, null, null
    );

    private static final Map<String, Ability> attributeMap = new HashMap<>() {{
        put("speed", SPEED);
        put("atkdmgpct", ATTACK_DAMAGE_PCT);
        put("atkdmgraw", ATTACK_DAMAGE_RAW);
        put("lightweight", LIGHTWEIGHT);
    }};

    private static final Map<Ability, Modifier> modifierMap = new HashMap<>() {{
       put(SPEED, new Modifier("itemabilities.speed", Attribute.GENERIC_MOVEMENT_SPEED , AttributeModifier.Operation.ADD_SCALAR));
       put(ATTACK_DAMAGE_PCT, new Modifier("itemabilities.attack_percent", Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_SCALAR));
       put(ATTACK_DAMAGE_RAW, new Modifier("itemabilities.attack_raw", Attribute.GENERIC_ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER));
       put(LIGHTWEIGHT, null);
    }};

    public static String getAttributeText(Ability attribute, int modifier) {
        return (modifier >= 0 ? ChatColor.GREEN : ChatColor.RED) + Integer.toString(modifier) + attribute.getAbilityName();
    }

    @Nullable
    public static Ability fromString(String s) {
        return attributeMap.get(s);
    }

    public static Set<String> getAvailableAttributeNames() {
        return attributeMap.keySet();
    }

    /**
     * @return The attribute this attribute is associated to, null if not found, or is done in another operation.
     */
    @Nullable
    public static Modifier getAttribute(Ability attribute) {
        return modifierMap.get(attribute);
    }

    public static class Modifier {
        final UUID uuid;
        final String name;
        final Attribute type;
        final AttributeModifier.Operation operation;

        public UUID getUuid() {
            return uuid;
        }

        public String getName() {
            return name;
        }

        public Attribute getType() {
            return type;
        }

        public AttributeModifier.Operation getOperation() {
            return operation;
        }

        public Modifier(String name, Attribute type, AttributeModifier.Operation operation) {
            this.uuid = UUID.randomUUID();
            this.name = name;
            this.type = type;
            this.operation = operation;
        }
    }
}

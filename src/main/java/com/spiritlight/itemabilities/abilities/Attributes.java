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
import java.util.logging.Level;

// Attributes can and should be assignable to breakable items.
public class Attributes {

    // Level 1 due to attributes not depending on enchantment
    public static final Ability SPEED = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_SPEED, "speed", "% Movement Speed",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    // Level 1 due to attributes not depending on enchantment
    public static final Ability ATTACK_DAMAGE_PCT = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ATTACK_DAMAGE_PCT, "atkdmgpct", "% Attack Damage",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    // Level 1 due to attributes not depending on enchantment
    public static final Ability ATTACK_DAMAGE_RAW = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ATTACK_DAMAGE_RAW, "atkdmgraw", " Raw Attack Damage",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    public static final Ability LIGHTWEIGHT = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_LIGHTWEIGHT, "lightweight", "% Reduced Fall Damage",
            new String[0], true, 32767, EnchantmentTarget.BREAKABLE,
            false
    );

    // You know the drill
    public static final Ability HEALTH = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_HEALTH, "health", " Health",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    public static final Ability ARMOR = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ARMOR, "armor", " Armor",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    public static final Ability TOUGHNESS = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_TOUGHNESS, "toughness", " Toughness",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE,
            false
    );

    public static final Ability ATTACK_SPEED_PERCENT = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ATTACK_SPEED, "attack_speed_pct", "% Attack Speed",
            new String[0], true, 1, EnchantmentTarget.BREAKABLE, false
    );

    public static final Ability ATTACK_SPEED_RAW = PluginWrapper.newAbility(
            ItemAbilities.ATTRIBUTE_ATTACK_SPEED_RAW, "attack_speed_raw", " Attack Speed",
            new String[0], false, 1, EnchantmentTarget.BREAKABLE, false
    );

    private static final Map<String, Ability> attributeMap = new HashMap<>() {{
        put("speed", SPEED);
        put("atkdmgpct", ATTACK_DAMAGE_PCT);
        put("atkdmgraw", ATTACK_DAMAGE_RAW);
        put("lightweight", LIGHTWEIGHT);
        put("health", HEALTH);
        put("armor", ARMOR);
        put("toughness", TOUGHNESS);
        put("attackspdpct", ATTACK_SPEED_PERCENT);
    }};

    private static final Map<Ability, Modifier> modifierMap = new HashMap<>() {{
        put(SPEED, new Modifier("itemabilities.speed",
                Attribute.GENERIC_MOVEMENT_SPEED,
                AttributeModifier.Operation.ADD_SCALAR));
        put(ATTACK_DAMAGE_PCT, new Modifier("itemabilities.attack_percent",
                Attribute.GENERIC_ATTACK_DAMAGE,
                AttributeModifier.Operation.ADD_SCALAR));
        put(ATTACK_DAMAGE_RAW, new Modifier("itemabilities.attack_raw",
                Attribute.GENERIC_ATTACK_DAMAGE,
                AttributeModifier.Operation.ADD_NUMBER));
        put(LIGHTWEIGHT, null);
        put(HEALTH, new Modifier("itemabilities.health",
                Attribute.GENERIC_MAX_HEALTH,
                AttributeModifier.Operation.ADD_NUMBER));
        put(ARMOR, new Modifier("itemabilities.armor",
                Attribute.GENERIC_ARMOR,
                AttributeModifier.Operation.ADD_NUMBER));
        put(TOUGHNESS, new Modifier("itemabilities.toughness",
                Attribute.GENERIC_ARMOR_TOUGHNESS,
                AttributeModifier.Operation.ADD_NUMBER));
        put(ATTACK_SPEED_PERCENT, new Modifier("itemabilities.attack_speed_percent",
                Attribute.GENERIC_ATTACK_SPEED,
                AttributeModifier.Operation.ADD_SCALAR));
        put(ATTACK_SPEED_RAW, new Modifier("itemabilities.attack_speed_raw",
                Attribute.GENERIC_ATTACK_SPEED,
                AttributeModifier.Operation.ADD_NUMBER));
    }};

    public static String getAttributeText(Ability attribute, int modifier) {
        ItemAbilities.logger.log(Level.INFO, "Acquiring attribute text with modifier " + modifier + " and attribute " + attribute.getAbilityName());
        return (modifier >= 0 ? ChatColor.GREEN + (modifier > 0 ? "+" : "") : ChatColor.RED) + Integer.toString(modifier) + ChatColor.GRAY + attribute.getAbilityName();
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

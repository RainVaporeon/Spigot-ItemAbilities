package com.spiritlight.itemabilities;

import com.spiritlight.itemabilities.abilities.*;
import com.spiritlight.itemabilities.commands.*;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Registering namespaces - {@link ItemAbilities#registerNameSpace()}
 * <br><br>
 * Registering commands - {@link ItemAbilities#registerCommands()}
 * <br><br>
 * Registering ability mapping - {@link ItemAbilities#registerAbilityMap()}
 * <br><br>
 * Event listeners - {@link ItemAbilities#registerEvents()}
 * <br><br>
 * Registering enchantments - {@link ItemAbilities#init()}
 */
public class ItemAbilities extends JavaPlugin {
    public static JavaPlugin INSTANCE;

    public static NamespacedKey ABILITY_TRACER;
    public static NamespacedKey ABILITY_GUARDIAN;
    public static NamespacedKey ABILITY_STRIKEBACK;
    public static NamespacedKey ABILITY_DOUBLESTRIKE;

    public static NamespacedKey ATTRIBUTE_SPEED;
    public static NamespacedKey ATTRIBUTE_ATTACK_DAMAGE_PCT;
    public static NamespacedKey ATTRIBUTE_ATTACK_DAMAGE_RAW;
    public static NamespacedKey ATTRIBUTE_LIGHTWEIGHT;
    public static NamespacedKey ATTRIBUTE_HEALTH;
    public static NamespacedKey ATTRIBUTE_ARMOR;
    public static NamespacedKey ATTRIBUTE_TOUGHNESS;

    public static NamespacedKey CURRENCY;

    public static final Map<String, Ability> abilityMap = new HashMap<>();

    private boolean initFinish = false;


    @Override
    public void onEnable() {
        INSTANCE = this;
        if(!initFinish) {
            try {
                init();
            } catch (ReflectiveOperationException e) {
                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
            }
        }
    }

    @Override
    public void onDisable() {

    }

    // registers enchantments etc
    private void init() throws ReflectiveOperationException {
        initFinish = true;
        registerNameSpace();
        registerAbilityMap();
        enchantLock(true);
        // Register abilities and attribute notes
        Enchantment.registerEnchantment(VAbilityTracer.ability);
        Enchantment.registerEnchantment(CAbilityGuardian.ability);
        Enchantment.registerEnchantment(CAbilityStrikeBack.ability);
        Enchantment.registerEnchantment(CAbilityDoubleStrike.ability);

        Enchantment.registerEnchantment(Attributes.SPEED);
        Enchantment.registerEnchantment(Attributes.ATTACK_DAMAGE_PCT);
        Enchantment.registerEnchantment(Attributes.ATTACK_DAMAGE_RAW);
        Enchantment.registerEnchantment(Attributes.LIGHTWEIGHT);
        Enchantment.registerEnchantment(Attributes.HEALTH);
        Enchantment.registerEnchantment(Attributes.ARMOR);
        Enchantment.registerEnchantment(Attributes.TOUGHNESS);

        Enchantment.registerEnchantment(EnchantmentUtils.CURRENCY);
        enchantLock(false);
        registerCommands();
        registerEvents();
    }

    private void registerNameSpace() {
        /* Abilities */
        ABILITY_TRACER = PluginWrapper.newNameSpacedKey("tracer");
        ABILITY_GUARDIAN = PluginWrapper.newNameSpacedKey("guardian");
        ABILITY_STRIKEBACK = PluginWrapper.newNameSpacedKey("strikeback");
        ABILITY_DOUBLESTRIKE = PluginWrapper.newNameSpacedKey("double_strike");

        /* Attributes */
        ATTRIBUTE_SPEED = PluginWrapper.newNameSpacedKey("speed");
        ATTRIBUTE_ATTACK_DAMAGE_PCT = PluginWrapper.newNameSpacedKey("atkdmgpct");
        ATTRIBUTE_ATTACK_DAMAGE_RAW = PluginWrapper.newNameSpacedKey("atkdmgraw");
        ATTRIBUTE_LIGHTWEIGHT = PluginWrapper.newNameSpacedKey("lightweight");
        ATTRIBUTE_HEALTH = PluginWrapper.newNameSpacedKey("health");
        ATTRIBUTE_ARMOR = PluginWrapper.newNameSpacedKey("armor");
        ATTRIBUTE_TOUGHNESS = PluginWrapper.newNameSpacedKey("toughness");

        /* Currency */
        CURRENCY = PluginWrapper.newNameSpacedKey("currency");
    }

    /**
     * Internal uses only, registers assignable abilities
     */
    private void registerAbilityMap() {
        abilityMap.put(VAbilityTracer.ability.getAbilityName(), VAbilityTracer.ability);
        abilityMap.put(CAbilityGuardian.ability.getAbilityName(), CAbilityGuardian.ability);
        abilityMap.put(CAbilityStrikeBack.ability.getAbilityName(), CAbilityStrikeBack.ability);
        abilityMap.put(CAbilityDoubleStrike.ability.getAbilityName(), CAbilityDoubleStrike.ability);
    }

    private void enchantLock(boolean access) throws ReflectiveOperationException {
        Field f = Enchantment.class.getDeclaredField("acceptingNew");
        f.setAccessible(true);
        f.set(null, access);
    }

    @SuppressWarnings("all")
    private void registerCommands() {
        this.setExecutorAndTabComplete("addability", new AddAbility());
        this.setExecutorAndTabComplete("removeability", new RemoveAbility());
        this.setExecutorAndTabComplete("addattribute", new AddAttribute());
        this.setExecutorAndTabComplete("removeattribute", new RemoveAttribute());
        this.setExecutorAndTabComplete("currency", new Currency());
    }

    // Register event listeners to make ability trigger
    private void registerEvents() {
        registerEvent(new VAbilityTracer());
        registerEvent(new CAbilityGuardian());
        registerEvent(new CAbilityLightweight());
        registerEvent(new CAbilityStrikeBack());
        registerEvent(new CAbilityDoubleStrike());
    }

    private void registerEvent(Listener l) {
        this.getServer().getPluginManager().registerEvents(l, this);
    }

    @SuppressWarnings("all")
    private void setExecutorAndTabComplete(String command, CommandBase handler) {
        this.getCommand(command).setExecutor(handler);
        this.getCommand(command).setTabCompleter(handler);
    }
}
package com.spiritlight.itemabilities;

import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Attributes;
import com.spiritlight.itemabilities.abilities.CAbilityGuardian;
import com.spiritlight.itemabilities.abilities.VAbilityTracer;
import com.spiritlight.itemabilities.commands.AddAbility;
import com.spiritlight.itemabilities.commands.AddAttribute;
import com.spiritlight.itemabilities.commands.RemoveAbility;
import com.spiritlight.itemabilities.commands.RemoveAttribute;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ItemAbilities extends JavaPlugin {
    public static JavaPlugin INSTANCE;
    public static NamespacedKey ABILITY_TRACER;
    public static NamespacedKey ABILITY_GUARDIAN;
    public static NamespacedKey ATTRIBUTE_SPEED;
    public static NamespacedKey ATTRIBUTE_ATTACK_DAMAGE_PCT;
    public static NamespacedKey ATTRIBUTE_ATTACK_DAMAGE_RAW;
    public static NamespacedKey ATTRIBUTE_LIGHTWEIGHT;
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
        Enchantment.registerEnchantment(VAbilityTracer.ability);
        Enchantment.registerEnchantment(CAbilityGuardian.ability);
        Enchantment.registerEnchantment(Attributes.SPEED);
        Enchantment.registerEnchantment(Attributes.ATTACK_DAMAGE_PCT);
        Enchantment.registerEnchantment(Attributes.ATTACK_DAMAGE_RAW);
        Enchantment.registerEnchantment(Attributes.LIGHTWEIGHT);
        enchantLock(false);
        registerCommands();
        registerEvents();
    }

    private void registerNameSpace() {
        ABILITY_TRACER = PluginWrapper.newNameSpacedKey("tracer");
        ABILITY_GUARDIAN = PluginWrapper.newNameSpacedKey("guardian");
        ATTRIBUTE_SPEED = PluginWrapper.newNameSpacedKey("speed");
        ATTRIBUTE_ATTACK_DAMAGE_PCT = PluginWrapper.newNameSpacedKey("atkdmgpct");
        ATTRIBUTE_ATTACK_DAMAGE_RAW = PluginWrapper.newNameSpacedKey("atkdmgraw");
        ATTRIBUTE_LIGHTWEIGHT = PluginWrapper.newNameSpacedKey("lightweight");
    }

    private void registerAbilityMap() {
        abilityMap.put(VAbilityTracer.ability.getAbilityName(), VAbilityTracer.ability);
        abilityMap.put(CAbilityGuardian.ability.getAbilityName(), CAbilityGuardian.ability);
    }

    private void enchantLock(boolean access) throws ReflectiveOperationException {
        Field f = Enchantment.class.getDeclaredField("acceptingNew");
        f.setAccessible(true);
        f.set(null, access);
    }

    @SuppressWarnings("all")
    private void registerCommands() {
        this.getCommand("addability").setExecutor(new AddAbility());
        this.getCommand("removeability").setExecutor(new RemoveAbility());
        this.getCommand("addability").setTabCompleter(new AddAbility());
        this.getCommand("removeability").setTabCompleter(new RemoveAbility());
        this.getCommand("addattribute").setExecutor(new AddAttribute());
        this.getCommand("removeattribute").setExecutor(new RemoveAttribute());
        this.getCommand("addattribute").setTabCompleter(new AddAttribute());
        this.getCommand("removeattribute").setTabCompleter(new RemoveAttribute());
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new VAbilityTracer(), this);
        this.getServer().getPluginManager().registerEvents(new CAbilityGuardian(), this);
    }
}
package com.spiritlight.itemabilities;

import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.CAbilityGuardian;
import com.spiritlight.itemabilities.abilities.VAbilityTracer;
import com.spiritlight.itemabilities.commands.AddAbility;
import com.spiritlight.itemabilities.commands.RemoveAbility;
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
    public static final Map<String, Ability> abilityMap = new HashMap<>();
    private boolean initFinish = false;


    @Override
    public void onEnable() {
        INSTANCE = this;
        abilityMap.clear();
        ABILITY_TRACER = PluginWrapper.newNameSpacedKey("tracer");
        ABILITY_GUARDIAN = PluginWrapper.newNameSpacedKey("guardian");
        abilityMap.put(VAbilityTracer.ability.getAbilityName(), VAbilityTracer.ability);
        abilityMap.put(CAbilityGuardian.ability.getAbilityName(), CAbilityGuardian.ability);
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

    // registers enchantments
    private void init() throws ReflectiveOperationException {
        initFinish = true;
        // Ignore add blocks
        Field f = Enchantment.class.getDeclaredField("acceptingNew");
        f.setAccessible(true);
        f.set(null, true);
        Enchantment.registerEnchantment(VAbilityTracer.ability);
        Enchantment.registerEnchantment(CAbilityGuardian.ability);
        f.set(null, false);
        registerCommands();
    }

    private void registerCommands() {
        this.getCommand("addability").setExecutor(new AddAbility());
        this.getCommand("removeability").setExecutor(new RemoveAbility());
        this.getCommand("addability").setTabCompleter(new AddAbility());
        this.getCommand("removeability").setTabCompleter(new RemoveAbility());
    }
}
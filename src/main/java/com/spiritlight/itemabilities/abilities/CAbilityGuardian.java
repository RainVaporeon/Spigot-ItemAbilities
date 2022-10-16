package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.EventListener;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class CAbilityGuardian extends EventListener {
    /**
     * Reduces incoming damage by 25%.<br>
     *
     * Not stackable.
     */
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_GUARDIAN, "guardian", "Guardian",
            "Reduces incoming damage by 25%\n Unstackable.", true,
            1, EnchantmentTarget.BREAKABLE, false, null, null
    );

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(((Player) event.getEntity()).getPlayer() == null) return;
        Player player = ((Player) event.getEntity()).getPlayer();
        if(player.getInventory().getItemInMainHand().containsEnchantment(ability)) {
            event.setDamage(event.getDamage() * 0.75);
        }
    }
}

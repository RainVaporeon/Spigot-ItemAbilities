package com.spiritlight.itemabilities.abilities;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class CAbilityGuardian implements Listener {
    /**
     * Reduces incoming damage by 25%.<br>
     *
     * Not stackable.
     */
    public static final Ability ability = PluginWrapper.newAbility(
            ItemAbilities.ABILITY_GUARDIAN, "guardian", "Guardian",
            new String[]{"Reduces incoming damage by 25%", ChatColor.GRAY + "(Unstackable)"}, true,
            1, EnchantmentTarget.BREAKABLE, false
    );

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;
        if(!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        if(PluginWrapper.containsEnchantment(player.getInventory().getItemInMainHand().getItemMeta(), ability)) {
            event.setDamage(event.getDamage() * 0.75);
        }
    }
}

package com.spiritlight.itemabilities.listeners;

import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class ItemUsedListener implements Listener {
    @EventHandler
    public void onItemUse(PlayerItemConsumeEvent event) {
        if(EnchantmentUtils.hasEnchant(event.getItem(), EnchantmentUtils.CURRENCY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(EnchantmentUtils.hasEnchant(event.getItemInHand(), EnchantmentUtils.CURRENCY)) {
            event.setCancelled(true);
        }
    }
}

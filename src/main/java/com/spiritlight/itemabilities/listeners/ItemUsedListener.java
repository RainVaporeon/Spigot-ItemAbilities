package com.spiritlight.itemabilities.listeners;

import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

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
            event.getBlockPlaced().setType(Material.AIR);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        for(ItemStack item : event.getInventory().getMatrix()) {
            if(item == null) continue;
            if(EnchantmentUtils.hasEnchant(item, EnchantmentUtils.CURRENCY)) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if(event.getItem() == null) return;
        // NonNull
        ItemStack stx = event.getPlayer().getInventory().getItemInMainHand().clone();
        stx.setAmount(1);
        // NonNull
        Player player = event.getPlayer();
        if(stx.getItemMeta() == null) return;
        if(!EnchantmentUtils.hasEnchant(stx, EnchantmentUtils.CURRENCY)) return;
        if(!event.getPlayer().isSneaking()) return;
        PluginWrapper.Currency currency = PluginWrapper.Currency.fromName(stx.getItemMeta().getDisplayName());
        if(currency == null) {
        }
        /*
        PluginWrapper.Currency exchange;

        // Extract
        if(event.getAction() == Action.LEFT_CLICK_AIR) {
            exchange = currency.getPrevious();
            if(exchange == null) return;
            int amount = currency.getAmount() / exchange.getAmount();
            player.getInventory().remove(stx);
            ItemStack exchangeCurrency = PluginWrapper.getCurrencyItem(exchange);
            exchangeCurrency.setAmount(amount);
            player.getInventory().addItem(exchangeCurrency);
            return;
            // Condense
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            exchange = currency.getNext();
            if(exchange == null) return;
            int requiredAmount = exchange.getAmount() / currency.getAmount();
            if(player.getInventory().getItemInMainHand().getAmount() < requiredAmount) return;
            stx.setAmount(requiredAmount);
            player.getInventory().remove(stx);
            player.getInventory().addItem(PluginWrapper.getCurrencyItem(exchange));
            return;
        }
        */
    }
}

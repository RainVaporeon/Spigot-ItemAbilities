package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.EnchantmentUtils;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class ExchangeCommand extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this!");
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (IndexOutOfBoundsException e) {
            amount = 1;
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid numerical input.");
            return true;
        }
        AtomicInteger potatoes = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents())
                .filter(item -> item != null &&
                        item.getType() == Material.POTATO &&
                        !item.hasItemMeta())
                .toList()
                .forEach(potato -> potatoes.addAndGet(potato.getAmount()));
        if(potatoes.get() < amount) {
            sender.sendMessage("You do not have enough potatoes for this exchange!");
            return true;
        }
        ItemStack removal = new ItemStack(Material.POTATO, amount);
        player.getInventory().removeItem(removal);
        PluginWrapper.getCurrency(player, amount);
        ItemAbilities.logger.log(Level.INFO, "Player " + player.getName() + " exchanged " + amount + " potatoes, has " + potatoes.get());
        sender.sendMessage(ChatColor.GREEN + "Successfully exchanged " + amount + " potatoes!");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

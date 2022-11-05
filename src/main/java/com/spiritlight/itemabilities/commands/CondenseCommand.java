package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Deprecated // Given up
public class CondenseCommand extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }
        int amount = 1; // Exchange quantity
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("all")) {
                int exchangeAmount = 0;
                for (ItemStack itemStack : player.getInventory().getContents()) {
                    if (!PluginWrapper.Currency.isCurrency(itemStack)) continue;
                    PluginWrapper.Currency currency = PluginWrapper.Currency.fromItemStack(itemStack);
                    exchangeAmount += currency.getAmount() * itemStack.getAmount();
                    player.getInventory().remove(itemStack);
                }
                PluginWrapper.getCurrency(player, exchangeAmount);
                player.sendMessage("Successfully condensed " + exchangeAmount + " potatoes!");
                return true;
            }
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        if (!PluginWrapper.Currency.isCurrency(handItem)) {
            sender.sendMessage("The item held is not a currency.");
            return true;
        }
        // Gets current item as a currency
        // assert non-null
        PluginWrapper.Currency currency = PluginWrapper.Currency.fromItemStack(handItem);
        if(currency == PluginWrapper.Currency.PLASMA) {
            sender.sendMessage("You already have the highest tiered currency!");
            return true;
        }
        // Quantity of items that should be removed
        int requiredAmount = amount * currency.getNext().getAmount() / currency.getAmount();
        if(requiredAmount > handItem.getAmount()) {
            sender.sendMessage("You do not have enough items!");
            return true;
        }
        ItemStack remove = handItem.clone();
        remove.setAmount(requiredAmount);
        player.getInventory().removeItem(remove);
        // Return a condensed amount, already auto-calculated.
        PluginWrapper.getCurrency(player, amount * currency.getNext().getAmount());
        sender.sendMessage("Successfully condensed " + amount + " potato currency!");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

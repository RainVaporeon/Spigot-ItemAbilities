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

@Deprecated // given up
public class ExtractCommand extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command!");
            return true;
        }
        int amount = 1;
        if (args.length != 0) {
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (!PluginWrapper.Currency.isCurrency(handItem)) {
            sender.sendMessage("The item held is not a currency.");
            return true;
        }
        if (handItem.getAmount() < amount) {
            sender.sendMessage("You do not have sufficient items!");
            return true;
        }
        PluginWrapper.Currency currency = PluginWrapper.Currency.fromName(handItem.getItemMeta().getDisplayName());
        if(currency == PluginWrapper.Currency.NORMAL) {
            sender.sendMessage("This is already the lowest tier of currency!");
            return true;
        }
        ItemStack remove = handItem.clone();
        remove.setAmount(amount);
        player.getInventory().remove(remove);
        amount *= currency.getAmount();
        PluginWrapper.getCurrency(player, amount, currency.getPrevious());
        sender.sendMessage("Successfully extracted " + amount + " potato currency!");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Currency extends CommandBase {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equals("currency")) return false;
        if(args.length != 1) return false;
        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this!");
            return true;
        }
        final int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount specified!");
            return true;
        }
        PluginWrapper.getCurrency((Player) sender, amount);
        sender.sendMessage("Successfully given " + amount + " currency!");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

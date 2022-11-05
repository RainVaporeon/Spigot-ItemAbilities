package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.utils.CommandBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetaDump extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("You may only run this command as a player!");
            return true;
        }
        try {
            player.sendMessage(player.getInventory().getItemInMainHand().getItemMeta().getAsString());
        } catch (Exception e) {
            player.sendMessage("null");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}

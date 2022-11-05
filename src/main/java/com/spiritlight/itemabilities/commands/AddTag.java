package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Tags;
import com.spiritlight.itemabilities.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public class AddTag extends CommandBase {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("You need to be a player to use this!");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage("You have to supply a tag!");
            return false;
        }
        if(!Tags.names.contains(args[0])) {
            sender.sendMessage("Invalid tag! Available: " + Tags.names);
            return true;
        }
        Ability tag = Tags.getTagByName(args[0]);
        if(tag == null) {
            sender.sendMessage("This tag doesn't exist! Available: " + Tags.names);
            return true;
        }
        ItemStack handItem = player.getInventory().getItemInMainHand();
        ItemMeta meta = handItem.getItemMeta();
        if(meta == null) {
            ItemAbilities.logger.log(Level.INFO, "Created new ItemMeta");
            meta = new SpiritItemMeta(handItem.getItemMeta());
        }
        if(PluginWrapper.containsEnchantment(meta, tag)) {
            sender.sendMessage("This item already has that tag!");
            return true;
        }
        if(tag.equals(Tags.IRREPAIRABLE)) {
            if(meta instanceof Repairable i) {
                i.setRepairCost(i.getRepairCost() + 100);
            } else {
                sender.sendMessage("This item does not seem to be repairable.");
                return true;
            }
        }
        if(tag.equals(Tags.UNBREAKABLE)) {
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        if(meta.hasLore()) {
            LoreBuilder builder = new LoreBuilder(meta.getLore());
            builder.append(tag.getAbilityDescription()[0]);
            meta.setLore(builder.build());
        } else {
            meta.setLore(Collections.singletonList(tag.getAbilityDescription()[0]));
        }
        meta.addEnchant(tag, 1, true);
        handItem.setItemMeta(meta);
        sender.sendMessage("Added tag!");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            return StringUtils.filterByRelevance(Tags.names, args[0]);
        }
        return Tags.names;
    }
}

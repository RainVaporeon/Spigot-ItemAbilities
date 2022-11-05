package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Attributes;
import com.spiritlight.itemabilities.utils.*;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class RemoveAttribute extends CommandBase {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("removeattribute")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command may not be executed by the console!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("You need to input an attribute name:");
            return false;
        }

        /* Initializing variables, determine the attribute and modifiers. */
        Ability ability = Attributes.fromString(args[0]);

        /* Checks whether the modifier is valid, we already are using a different ability map here! */
        if (ability == null) {
            sender.sendMessage("You need to specify a correct ability name!");
            sender.sendMessage("Available options: " + Attributes.getAvailableAttributeNames());
            return true;
        }

        /* Starts adding attributes etc */
        try {
            ItemStack i = ((Player) sender).getInventory().getItemInMainHand();
            ItemMeta meta = i.getItemMeta();
            if (!PluginWrapper.containsEnchantment(meta, ability)) {
                sender.sendMessage("This attribute does not exist!");
                return true;
            }
            if (meta == null) {
                ItemAbilities.logger.log(Level.INFO, "Item has no meta. Creating a new one.");
                meta = new SpiritItemMeta(i.getItemMeta());
            }
            // assert i.getItemMeta() != null
            if (meta.hasLore()) {
                LoreBuilder lb = new LoreBuilder(meta.getLore())
                        .removeLine(Attributes.getAttributeText(ability, EnchantmentUtils.getEnchantmentLevel(i, ability)));
                meta.setLore(lb.build());
                ItemAbilities.logger.log(Level.INFO, "Removed lore!");
            }
            Attributes.Modifier attribute = Attributes.getAttribute(ability);
            if (attribute != null) {
                // Generally speaking we should have only one attribute
                try {
                    AttributeModifier trim = meta.getAttributeModifiers().get(attribute.getType()).stream().filter(
                            a -> a.getName().equals(attribute.getName())
                    ).toList().get(0);
                    meta.removeAttributeModifier(attribute.getType(), trim);
                } catch (Exception e) {
                    sender.sendMessage("There doesn't seem to be an attribute associated to this item.");
                }
            }
            meta.removeEnchant(ability);
            i.setItemMeta(meta);
            sender.sendMessage("The ability has been removed from this item!");
            return true;
        } catch (Exception t) {
            t.printStackTrace();
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1)
            return StringUtils.filterByRelevance(Attributes.getAvailableAttributeNames().stream().toList(), args[0]);
        return Collections.emptyList();
    }
}

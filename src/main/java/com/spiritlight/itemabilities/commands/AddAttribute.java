package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Attributes;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import com.spiritlight.itemabilities.utils.SpiritItemMeta;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class AddAttribute extends CommandBase {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("addattribute")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command may not be executed by the console!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("You need to input an attribute name:");
            return false;
        }
        if(args.length == 1) {
            sender.sendMessage("You need to input an attribute value.");
            return false;
        }

        /* Initializing variables, determine the attribute and modifiers. */
        Ability ability = Attributes.fromString(args[0]);
        int modifier;
        int level = 1;
        try {
            modifier = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage("Cannot parse the given modifier!");
            return true;
        }

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
            if (PluginWrapper.containsEnchantment(i, ability)) {
                sender.sendMessage("You cannot have duplicate attributes!");
                return true;
            }
            if (meta == null) {
                ItemAbilities.logger.log(Level.INFO, "Item has no meta. Creating a new one.");
                meta = new SpiritItemMeta(i.getItemMeta());
            }
            // assert i.getItemMeta() != null
            if (meta.hasLore()) {
                List<String> lore = Objects.requireNonNull(meta.getLore());
                lore.add(Attributes.getAttributeText(ability, modifier));
                meta.setLore(lore);
                ItemAbilities.logger.log(Level.INFO, "Appended lore!");
            } else {
                ItemAbilities.logger.log(Level.INFO, "Added lore!");
                meta.setLore(Collections.singletonList(Attributes.getAttributeText(ability, modifier)));
            }
            Attributes.Modifier attribute = Attributes.getAttribute(ability);
            if (attribute == null) {
                if(ability == Attributes.LIGHTWEIGHT) {
                    level = modifier; // Lightweight uses reduction based on level, 1% per level.
                }
            } else {
                try {
                    meta.addAttributeModifier(attribute.getType(), new AttributeModifier(
                            attribute.getUuid(), attribute.getName(),
                            attribute.getOperation() == AttributeModifier.Operation.ADD_NUMBER ? modifier : modifier / 100.0F,
                            attribute.getOperation()));
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("There seems to be an modifier that already exists!");
                    return false;
                }
            }
            i.setItemMeta(meta);
            ((Player) sender).getInventory().getItemInMainHand().setItemMeta(meta);
            i.addEnchantment(ability, level);
            sender.sendMessage("The ability has been added to this item!");
            return true;
        } catch (IllegalArgumentException e) {
          sender.sendMessage("Invalid modifier! (Is it too high, or too low?)");
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
            return Attributes.getAvailableAttributeNames().stream().toList();
        return Collections.emptyList();
    }
}

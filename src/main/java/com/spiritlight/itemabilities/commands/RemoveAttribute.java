package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.Attributes;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import com.spiritlight.itemabilities.utils.SpiritItemMeta;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RemoveAttribute implements CommandExecutor, TabCompleter {
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
            if (PluginWrapper.containsEnchantment(i, ability)) {
                sender.sendMessage("You cannot have duplicate attributes!");
                return true;
            }
            if (meta == null) {
                System.out.println("Item has no meta. Creating a new one.");
                meta = new SpiritItemMeta(i.getItemMeta());
            }
            // assert i.getItemMeta() != null
            if (meta.hasLore()) {
                List<String> lore = Objects.requireNonNull(meta.getLore());
                lore.remove(Attributes.getAttributeText(ability, meta.getEnchantLevel(ability)));
                meta.setLore(lore);
                System.out.println("Removed lore!");
            }
            Attributes.Modifier attribute = Attributes.getAttribute(ability);
            if (attribute != null) {
                // Generally speaking we should have only one attribute
                meta.removeAttributeModifier(attribute.getType(), new AttributeModifier(attribute.getUuid(),
                        attribute.getName(),
                        meta.getAttributeModifiers(attribute.getType())
                                .stream()
                                .filter(f -> f.getUniqueId().equals(attribute.getUuid()))
                                .toList().get(0).getAmount(),
                        attribute.getOperation()));
            }
            i.setItemMeta(meta);
            ((Player) sender).getInventory().getItemInMainHand().setItemMeta(meta);
            i.removeEnchantment(ability); // Lightweight also gets removed
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
            return Attributes.getAvailableAttributeNames().stream().toList();
        return Collections.emptyList();
    }
}

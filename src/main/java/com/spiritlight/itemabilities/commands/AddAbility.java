package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.PluginWrapper;
import com.spiritlight.itemabilities.utils.SpiritItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class AddAbility extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("addability")) return false;
        if(!(sender instanceof Player)) {
            sender.sendMessage("This command may not be executed by the console!");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage("You need to input an ability name:");
            return false;
        }
        Ability ability = ItemAbilities.abilityMap.get(args[0]);
        if(ability == null) {
            sender.sendMessage("You need to specify a correct ability name!");
            sender.sendMessage("Available options: " + ItemAbilities.abilityMap.keySet());
            return true;
        }
        try {
            ItemStack i = ((Player) sender).getInventory().getItemInMainHand();
            ItemMeta meta = i.getItemMeta();
            if(!ability.getItemTarget().includes(i)) {
                sender.sendMessage("This ability is incompatible with this item!");
                return true;
            }
            if(PluginWrapper.containsEnchantment(i, ability)) {
                sender.sendMessage("You cannot have duplicate abilities!");
                return true;
            }
            if(meta == null) {
                ItemAbilities.logger.log(Level.INFO, "Item has no meta. Creating a new one.");
                meta = new SpiritItemMeta(i.getItemMeta());
            }
            // assert i.getItemMeta() != null
            if(ability.abilityVisible()) {
                if(meta.hasLore()) {
                    List<String> lore = Objects.requireNonNull(meta.getLore());
                    lore.addAll(ability.descriptionList);
                    meta.setLore(lore);
                    ItemAbilities.logger.log(Level.INFO, "Appended lore!");
                } else {
                    ItemAbilities.logger.log(Level.INFO, "Added lore!");
                    meta.setLore(ability.descriptionList);
                }
            }
            i.setItemMeta(meta);
            ((Player) sender).getInventory().getItemInMainHand().setItemMeta(meta);
            i.addEnchantment(ability, 1);
            sender.sendMessage("The ability has been added to this item!");
            return true;
        } catch (Exception t) {
            t.printStackTrace();
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return ItemAbilities.abilityMap.keySet().stream().toList();
    }
}

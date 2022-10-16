package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.SpiritItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
            if(i.containsEnchantment(ability)) {
                sender.sendMessage("You cannot have duplicate abilities!");
                return true;
            }
            i.addEnchantment(ability, 1);
            if(!i.hasItemMeta()) {
                System.out.println("Item has no meta. Creating a new one.");
                i.setItemMeta(new SpiritItemMeta());
            }
            // assert i.getItemMeta() != null
            if(i.getItemMeta().hasLore()) {
                List<String> lore = Objects.requireNonNull(i.getItemMeta().getLore());
                lore.add("");
                lore.addAll(ability.descriptionList);
                i.getItemMeta().setLore(lore);
            } else {
                System.out.println("Added lore!");
                i.getItemMeta().setLore(ability.descriptionList);
            }
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

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

public class RemoveAbility extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!command.getName().equalsIgnoreCase("removeability")) return false;
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
            return true;
        }
        try {
            ItemStack i = ((Player) sender).getInventory().getItemInMainHand();
            if(!i.containsEnchantment(ability)) {
                sender.sendMessage("This ability is not linked to this item!");
                return true;
            }
            i.removeEnchantment(ability);
            if(!i.hasItemMeta()) {
                i.setItemMeta(new SpiritItemMeta());
            }
            // assert i.getItemMeta() != null
            if(i.getItemMeta().hasLore()) {
                List<String> lore = i.getItemMeta().getLore();
                lore.removeIf(ability.descriptionList::contains);
                i.getItemMeta().setLore(lore);
            }
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
        return ItemAbilities.abilityMap.keySet().stream().toList();
    }
}

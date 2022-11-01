package com.spiritlight.itemabilities.commands;
import com.spiritlight.itemabilities.ItemAbilities;
import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.SpiritItemMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

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
            sender.sendMessage("Available options: " + ItemAbilities.abilityMap.keySet());
            return true;
        }
        try {
            ItemStack i = ((Player) sender).getInventory().getItemInMainHand();
            ItemMeta meta = i.getItemMeta();
            if(meta == null) {
                meta = new SpiritItemMeta(i.getItemMeta());
            }
            if(i.removeEnchantment(ability) == 0) {
                sender.sendMessage("This ability is not linked to this item!");
                return true;
            }
            // assert meta() != null
            if(meta.getLore() != null && ability.abilityVisible()) {
                List<String> lore = meta.getLore();
                lore.removeIf(ability.descriptionList::contains);
                lore.remove(lore.size() - 1);
                meta.setLore(lore);
                ItemAbilities.logger.log(Level.INFO, "Lore removed!");
            }
            i.setItemMeta(meta);
            ((Player) sender).getInventory().getItemInMainHand().setItemMeta(meta);
            i.removeEnchantment(ability);
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

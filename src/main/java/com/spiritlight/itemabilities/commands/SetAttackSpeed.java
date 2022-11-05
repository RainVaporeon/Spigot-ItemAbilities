package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.abilities.Ability;
import com.spiritlight.itemabilities.abilities.AttackSpeed;
import com.spiritlight.itemabilities.abilities.Attributes;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.LoreBuilder;
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

import java.util.List;

public class SetAttackSpeed extends CommandBase {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this!");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage("Invalid attack speed. Available: " + AttackSpeed.getNames());
            return false;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) {
            meta = new SpiritItemMeta(itemStack.getItemMeta());
        }
        AttackSpeed attackSpeed = AttackSpeed.fromName(args[0]);
        if(attackSpeed == null) {
            sender.sendMessage("Invalid attack speed. Available: " + AttackSpeed.getNames());
            return false;
        }
        LoreBuilder builder = new LoreBuilder(meta.getLore());
        builder.setAttackSpeed(attackSpeed);
        Attributes.Modifier attribute = Attributes.getAttribute(Attributes.ATTACK_SPEED_PERCENT);
        meta.addAttributeModifier(attribute.getType(), new AttributeModifier(
                attribute.getUuid(), attribute.getName(),
                attackSpeed.getModifier(),
                attribute.getOperation()));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        sender.sendMessage("Successfully set item attack speed.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return AttackSpeed.getNames();
    }
}

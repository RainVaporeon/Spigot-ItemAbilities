package com.spiritlight.itemabilities.commands;

import com.spiritlight.itemabilities.abilities.AttackSpeed;
import com.spiritlight.itemabilities.abilities.Attributes;
import com.spiritlight.itemabilities.utils.CommandBase;
import com.spiritlight.itemabilities.utils.LoreBuilder;
import com.spiritlight.itemabilities.utils.SpiritItemMeta;
import com.spiritlight.itemabilities.utils.StringUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
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
        Attributes.Modifier attribute = Attributes.getAttribute(Attributes.ATTACK_SPEED_RAW);
        // Assert such that no duplicate modifiers exist
        if(meta.getAttributeModifiers() != null) {
            List<AttributeModifier> lst = meta.getAttributeModifiers().get(Attribute.GENERIC_ATTACK_SPEED).stream()
                    .filter(attr -> attr.getName().equals(attribute.getName()))
                    .toList();
            if(lst.size() != 0)
                meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, lst.get(0));
        }
        meta.addAttributeModifier(attribute.getType(), new AttributeModifier(
                attribute.getUuid(), attribute.getName(),
                attackSpeed.getModifier(),
                attribute.getOperation(),
                EquipmentSlot.HAND));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(builder.build());
        itemStack.setItemMeta(meta);
        sender.sendMessage("Successfully set item attack speed.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            return StringUtils.filterByRelevance(AttackSpeed.getNames(), args[0]);
        }
        return AttackSpeed.getNames();
    }
}

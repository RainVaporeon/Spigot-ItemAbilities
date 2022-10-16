package com.spiritlight.itemabilities.utils;

import com.google.common.collect.Multimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * An easy implementation of ItemMeta.
 */
@SuppressWarnings("MethodDoesntCallSuperMethod")
public class SpiritItemMeta implements ItemMeta {
    private String displayName = "";
    private List<String> lore = Collections.emptyList();
    private String localizedName = "";
    private int modelData = 0;
    private Map<Enchantment, Integer> enchantments = Collections.emptyMap();
    private Set<ItemFlag> itemFlags = Collections.emptySet();
    private boolean unbreakable = false;
    @Nullable
    private Multimap<Attribute, AttributeModifier> attributeMap = null;

    public SpiritItemMeta() {}

    public SpiritItemMeta(ItemMeta meta) { fromItemMeta(meta); }

    /**
     * Sets the current object into the ItemMeta provided.
     * @param meta The meta to clone itself into
     */
    public final void fromItemMeta(ItemMeta meta) {
        if(meta == null) return;
        lore = meta.getLore();
        displayName = meta.getDisplayName();
        localizedName = meta.getLocalizedName();
        modelData = meta.getCustomModelData();
        enchantments = meta.getEnchants();
        itemFlags = meta.getItemFlags();
        unbreakable = meta.isUnbreakable();
        attributeMap = meta.getAttributeModifiers();
    }


    @Override
    public boolean hasDisplayName() {
        return displayName == null || displayName.length() == 0;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return StringUtils.stringOrNull(displayName);
    }

    @Override
    public void setDisplayName(@org.jetbrains.annotations.Nullable String name) {
        this.displayName = name;
    }

    @Override
    public boolean hasLocalizedName() {
        return getLocalizedName().length() != 0 && !getLocalizedName().equals("null");
    }

    @NotNull
    @Override
    public String getLocalizedName() {
        return StringUtils.nullOrDefault(localizedName, getDisplayName());
    }

    @Override
    public void setLocalizedName(@org.jetbrains.annotations.Nullable String name) {
        this.localizedName = name;
    }

    @Override
    public boolean hasLore() {
        return lore.size() != 0;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public List<String> getLore() {
        return lore;
    }

    @Override
    public void setLore(@org.jetbrains.annotations.Nullable List<String> loreIn) {
        lore = loreIn;
    }

    @Override
    public boolean hasCustomModelData() {
        return modelData != 0;
    }

    @Override
    public int getCustomModelData() {
        return modelData;
    }

    @Override
    public void setCustomModelData(@org.jetbrains.annotations.Nullable Integer data) {
        this.modelData = data == null ? 0 : data;
    }

    @Override
    public boolean hasEnchants() {
        return enchantments.isEmpty();
    }

    @Override
    public boolean hasEnchant(@NotNull Enchantment ench) {
        return enchantments.containsKey(ench);
    }

    @Override
    public int getEnchantLevel(@NotNull Enchantment ench) {
        return enchantments.get(ench);
    }

    @NotNull
    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return enchantments;
    }

    @Override
    public boolean addEnchant(@NotNull Enchantment ench, int level, boolean ignoreLevelRestriction) {
        enchantments.put(ench, level);
        return true;
    }

    @Override
    public boolean removeEnchant(@NotNull Enchantment ench) {
        enchantments.remove(ench);
        return false;
    }

    @Override
    public boolean hasConflictingEnchant(@NotNull Enchantment ench) {
        boolean flag;
        for (Enchantment e : enchantments.keySet()) {
            flag = e.conflictsWith(ench);
            if (flag) return true;
        }
        return false;
    }

    @Override
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        this.itemFlags.addAll(List.of(itemFlags));
    }

    @Override
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        this.itemFlags.removeIf(flag -> {
            for(ItemFlag f : itemFlags) {
                if(flag.equals(f)) return true;
            }
            return false;
        });
    }

    @NotNull
    @Override
    public Set<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag flag) {
        return itemFlags.contains(flag);
    }

    @Override
    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public boolean hasAttributeModifiers() {
        return attributeMap != null && attributeMap.isEmpty();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return attributeMap;
    }

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot) {
        throw new UnsupportedOperationException();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@NotNull Attribute attribute) {
        return attributeMap == null ? null : attributeMap.get(attribute);
    }

    @Override
    public boolean addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        return attributeMap != null && attributeMap.put(attribute, modifier);
    }

    @Override
    public void setAttributeModifiers(@org.jetbrains.annotations.Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        attributeMap = attributeModifiers;
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute) {
        if (attributeMap != null)
            attributeMap.removeAll(attribute);
        else return false;
        return true;
    }

    @Override
    public boolean removeAttributeModifier(@NotNull EquipmentSlot slot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        return attributeMap != null && attributeMap.remove(attribute, modifier);
    }

    /**
     * It's required that this be overridden if NBT is required.
     */
    @NotNull
    @Override
    public String getAsString() {
        return "{}";
    }

    @NotNull
    @Override
    public CustomItemTagContainer getCustomTagContainer() {
        throw new UnsupportedOperationException("This operation is deprecated!");
    }

    @Override
    public void setVersion(int version) {

    }

    @NotNull
    @Override
    public ItemMeta clone() {
        return new SpiritItemMeta(this);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Collections.emptyMap();
    }

    @NotNull
    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        throw new UnsupportedOperationException();
    }
}

package com.spiritlight.itemabilities.utils;

import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StringUtils {
    public static String stringOrNull(String s) {
        return s == null ? "null" : s;
    }

    public static String nullOrDefault(String s, String _default) {
        return s == null ? stringOrNull(_default) : s;
    }

    public static List<String> filterByRelevance(final List<String> in, final String key) {
        return in.stream().filter(
                string -> string.toLowerCase(Locale.ROOT).startsWith(key.toLowerCase(Locale.ROOT))
        ).toList();
    }

    @Nullable
    public static EquipmentSlot getSlotByString(String s) {
        return equipmentMap.get(s.toUpperCase(Locale.ROOT));
    }

    public static final Map<String, EquipmentSlot> equipmentMap = new HashMap<>() {{
       put("HAND", EquipmentSlot.HAND);
       put("OFFHAND", EquipmentSlot.OFF_HAND);
       put("HEAD", EquipmentSlot.HEAD);
       put("CHEST", EquipmentSlot.CHEST);
       put("LEGS", EquipmentSlot.LEGS);
       put("FEET", EquipmentSlot.FEET);
    }};
}

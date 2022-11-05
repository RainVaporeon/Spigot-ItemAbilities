package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.abilities.AttackSpeed;
import org.apache.commons.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LoreBuilder {
    private final List<String> lore;

    public LoreBuilder(String... lore) {
        List<String> ret = new ArrayList<>();
        Collections.addAll(ret, lore);
        this.lore = ret;
    }

    public LoreBuilder(List<String> lore) {
        this.lore = Objects.requireNonNullElseGet(lore, ArrayList::new);
    }

    // Appends 2 lines
    public LoreBuilder setAttackSpeed(AttackSpeed attackSpeed) {
        if(lore.size() != 0) {
            lore.set(0, attackSpeed.getFormattedName());
            lore.add(" ");
        }
        else
            lore.add(attackSpeed.getFormattedName());
        return this;
    }

    public LoreBuilder addFirst(String s) {
        if(lore.contains("Attack Speed")) {
            lore.add(2, s);
        } else
            lore.add(0, s);
        return this;
    }

    public LoreBuilder append(String s) {
        lore.add(s);
        return this;
    }


    public LoreBuilder addAll(List<String> s) {
        lore.addAll(s);
        return this;
    }

    public LoreBuilder removeLine(String s) {
        lore.remove(s);
        return this;
    }

    public List<String> findSimilar(String s) {
        return lore.stream()
                .filter(string -> string.contains(s))
                .toList();
    }

    public List<String> build() {
        return lore;
    }
}

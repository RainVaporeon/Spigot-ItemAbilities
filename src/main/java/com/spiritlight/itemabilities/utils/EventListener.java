package com.spiritlight.itemabilities.utils;

import com.spiritlight.itemabilities.interfaces.annotations.AutoSubscribe;
import org.bukkit.event.Listener;

/**
 * This abstract implementation effectively implements the {@link Listener} interface.<br><br>
 *
 * The specific implementation also allows the use of custom annotations in-plugin. i.e. {@link AutoSubscribe}
 * and such.
 */
public abstract class EventListener implements Listener {
    public EventListener() {
        if(this.getClass().isAnnotationPresent(AutoSubscribe.class))
            PluginWrapper.subscribeEvents(this);
    }
}

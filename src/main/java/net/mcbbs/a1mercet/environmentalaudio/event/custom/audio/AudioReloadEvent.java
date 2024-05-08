package net.mcbbs.a1mercet.environmentalaudio.event.custom.audio;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AudioReloadEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    public AudioReloadEvent()
    {
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled=b;
    }
}

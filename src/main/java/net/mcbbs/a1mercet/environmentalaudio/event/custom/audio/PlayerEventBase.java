package net.mcbbs.a1mercet.environmentalaudio.event.custom.audio;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerEventBase extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    public PlayerEventBase(Player who)
    {
        super(who);
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

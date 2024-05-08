package net.mcbbs.a1mercet.environmentalaudio.event;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioData;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class EventAudio implements Listener
{
    public static AudioManager.AudioOptions options = AudioManager.options;

    public static void onJoinHandle(Player p)
    {
        AudioManager.createPlayerState(p);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt)
    {
        onJoinHandle(evt.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt)
    {
        Player p = evt.getPlayer();
        AudioManager.removePlayerState(p);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent evt)
    {
        if(evt.isCancelled() || options.enableTickCheck)return;

        Player p = evt.getPlayer();
        PlayerAudioState ps = AudioManager.getPlayerState(p);
        long moveTick = ps.moveTick;
        if(moveTick>=Long.MAX_VALUE-1L){ps.moveTick=0L;ps.moveTickRound++;}

        if(moveTick% options.threshold==0L)
            checkPlayer(ps);
        ps.moveTick++;
    }
    public static void checkPlayer(PlayerAudioState ps)
    {
        if(ps==null)return;
        HashMap<AudioState,Boolean> active = AudioManager.getActive(ps);
        for (AudioState state : active.keySet())
            if(active.get(state))   state.play(ps);
            else                    state.stop(ps);

        for(AudioState state : ps.cycle.values())
        {
            AudioData data = state.getData();
            if(ps.moveTick%Math.max(1,data.cycleDelay)==0)
                state.playBypass(ps);
        }

    }

    public static void checkPlayers(String world){checkPlayers(Bukkit.getWorld(world));}
    public static void checkPlayers(World world)
    {
        if(world==null)return;
        for(Player p : world.getPlayers())
            checkPlayer(AudioManager.getPlayerState(p));
    }
    public static void checkPlayers(){
        for(PlayerAudioState p : AudioManager.getPlayerStates().values())
            checkPlayer(p);
    }
}

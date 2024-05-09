package net.mcbbs.a1mercet.environmentalaudio.event;

import net.mcbbs.a1mercet.environmentalaudio.config.AudioLoader;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioData;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioEntity;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class EventAudio implements Listener
{
    public static AudioManager.AudioOptions options = AudioManager.options;

    @EventHandler
    public void onPluginEnable(PluginEnableEvent evt)
    {
        boolean reload = true;

        if(AudioType.registerPlugins.containsKey(evt.getPlugin()))
            AudioType.registerPlugins.put(evt.getPlugin(),true);

        for(Plugin plugin : new ArrayList<>(AudioType.registerPlugins.keySet()))
            if(!AudioType.registerPlugins.getOrDefault(plugin,false))
                reload = false;
        if(reload) AudioLoader.reload();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent evt)
    {
        String spawnName = evt.getEntity().getName();
        String spawnUUID = evt.getEntity().getUniqueId().toString();

        for(AudioState state : AudioManager.getRegisterStatesID().values())
            if(state instanceof AudioEntity.AudioStateEntity)
            {
                String e = ((AudioEntity.AudioStateEntity) state).entity;
                if(spawnUUID.equals(e) || spawnName.equals(e))
                    ((AudioEntity.AudioStateEntity) state).setEntity(evt.getEntity());
            }

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent evt)
    {
        if(!(evt.getEntity() instanceof Player))
        {
            String deathName = evt.getEntity().getName();
            String deathUUID = evt.getEntity().getUniqueId().toString();

            for(AudioState state : AudioManager.getRegisterStatesID().values())
                if(state instanceof AudioEntity.AudioStateEntity)
                {
                    String e = ((AudioEntity.AudioStateEntity) state).entity;
                    if(deathUUID.equals(e) || deathName.equals(e))
                        ((AudioEntity.AudioStateEntity) state).setEntity(null);
                }

        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent evt)
    {
        if(!evt.getFrom().getWorld().equals(evt.getTo().getWorld()))
        {
            PlayerAudioState ps = AudioManager.getPlayerState(evt.getPlayer());
            if(ps!=null) ps.clearAll();
        }
    }

    public static void onJoinHandle(Player p)
    {
        AudioManager.createPlayerState(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent evt)
    {
        Player p = evt.getPlayer();

        onJoinHandle(evt.getPlayer());

        try {

            for(AudioState state : AudioManager.getRegisterStatesID().values())
                if(state instanceof AudioEntity.AudioStateEntity)
                    if(("player@"+evt.getPlayer().getName()).equals(((AudioEntity)state.audio).entity))
                        ((AudioEntity.AudioStateEntity) state).setEntity(p);

        }catch (Exception e){e.printStackTrace();}
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent evt)
    {
        Player p = evt.getPlayer();

        try {

            for(AudioState state : AudioManager.getRegisterStatesID().values())
                if(state instanceof AudioEntity.AudioStateEntity)
                    if(("player@"+evt.getPlayer().getName()).equals(((AudioEntity)state.audio).entity))
                        ((AudioEntity.AudioStateEntity) state).setEntity(null);

        }catch (Exception e){e.printStackTrace();}

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
            {
                state.playBypass(ps);
                ps.processCycleRounds(state.id,1);
            }
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

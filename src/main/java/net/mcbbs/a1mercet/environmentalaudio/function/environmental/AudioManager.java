package net.mcbbs.a1mercet.environmentalaudio.function.environmental;

import net.mcbbs.a1mercet.environmentalaudio.EnvironmentalAudio;
import net.mcbbs.a1mercet.environmentalaudio.config.AudioLoader;
import net.mcbbs.a1mercet.environmentalaudio.event.EventAudio;
import net.mcbbs.a1mercet.environmentalaudio.function.area.Area;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioManager
{
    public static class AudioOptions
    {
        public long lastReload          = System.currentTimeMillis();
        public List<File> registerFile  = new ArrayList<>();

        public boolean enableFileCheck  = true;
        public int threshold            = 2;
        public boolean debug            = true;
        public boolean enableTickCheck  = true;
        public String path              = null;
        public BukkitTask tickHandler   = null;


        public void saveDefaultConfig()
        {
            //todo
        }


    }

    public static class TickHandler extends BukkitRunnable
    {
        @Override
        public void run() {

            if(options.enableTickCheck)
            {
                for(PlayerAudioState s : AudioManager.getPlayerStates().values())
                {
                    try {
                        if(s.moveTick>=Long.MAX_VALUE-1L-options.threshold){s.moveTick=0L;s.moveTickRound++;}
                        s.moveTick += options.threshold;
                        EventAudio.checkPlayers();
                    }catch (Exception e){e.printStackTrace();}

                }
                checkFileModify();
            }

            for(AudioState state : AudioManager.registerStatesID.values())
                if(state instanceof AudioEntity.AudioStateEntity)
                    ((AudioEntity.AudioStateEntity) state).resetLocation();
        }

        public void checkFileModify()
        {
            try {

                for(File f : options.registerFile)
                    if(f!=null && !f.getName().equals("custom.yml"))
                        if(f.lastModified() > options.lastReload)
                        {
                            AudioLoader.reload();
                            break;
                        }

            }catch (Exception e){e.printStackTrace();}
        }
    }


    public static Audio amb_beach;
    public static Audio amb_factory;
    public static Audio amb_forest;
    public static Audio amb_indoor;
    public static Audio amb_indoor_wind;
    public static Audio amb_outdoor_rain_mid;
    public static Audio amb_outdoor_rain_strong;
    public static Audio amb_outdoor_rain_weak;
    public static Audio amb_river;
    public static Audio amb_seagulls;
    public static Audio music_penis;

    public static void initDefaultAudio()
    {
        amb_beach                       = new Audio("germmod:amb_beach","沙滩");
        amb_factory                     = new Audio("germmod:amb_factory","沙滩");
        amb_forest                      = new Audio("germmod:amb_forest","沙滩");
        amb_indoor                      = new Audio("germmod:amb_indoor","沙滩");
        amb_indoor_wind                 = new Audio("germmod:amb_indoor_wind","沙滩");
        amb_outdoor_rain_mid            = new Audio("germmod:amb_outdoor_rain_mid","沙滩");
        amb_outdoor_rain_strong         = new Audio("germmod:amb_outdoor_rain_strong","沙滩");
        amb_outdoor_rain_weak           = new Audio("germmod:amb_outdoor_rain_weak","沙滩");
        amb_river                       = new Audio("germmod:amb_river","沙滩");
        amb_seagulls                    = new Audio("germmod:amb_seagulls","沙滩");
        music_penis                     = new Audio("germmod:music_penis","PenisMusic");
        music_penis.data.range          = 8;
        music_penis.data.volume         = 0.5F;
    }

    protected static final HashMap<String, Area> areas = new HashMap<>();
    public static HashMap<String,Area> getAreas(){return areas;}
    public static Area getArea(String id){return areas.get(id);}
    public static void putArea(Area a){
        if(areas.containsKey(a.id)){Bukkit.getLogger().warning("[AudioManager]区域 ["+a.id+"] 重复注册");}
        areas.put(a.id,a);
    }

    protected static final HashMap<String,PlayerAudioState> playerStates = new HashMap<>();
    protected static final HashMap<String, Audio> audios = new HashMap<>();
    protected static final HashMap<String, List<AudioState>> registerStates = new HashMap<>();
    protected static final HashMap<String, AudioState>       registerStatesID = new HashMap<>();
    public static final AudioOptions options = new AudioOptions();

    public static void init()
    {
        initDefaultAudio();
        if(options.tickHandler!=null)   options.tickHandler.cancel();
        options.tickHandler = new TickHandler().runTaskTimer(EnvironmentalAudio.getInstance(),0,options.threshold);
    }

    public static HashMap<String,PlayerAudioState> getPlayerStates(){return playerStates;}
    public static PlayerAudioState getPlayerState(String p){return playerStates.get(p);}
    public static PlayerAudioState getPlayerState(Player p){return playerStates.get(p.getName());}
    public static PlayerAudioState removePlayerState(String p){return playerStates.remove(p);}
    public static PlayerAudioState removePlayerState(Player p){return playerStates.remove(p.getName());}
    public static PlayerAudioState createPlayerState(Player p){PlayerAudioState s = new PlayerAudioState(p);playerStates.put(p.getName(),s);return s;}


    public static HashMap<String, Audio> getAudios(){return audios;}
    public static Audio getAudio(String id){return audios.get(id);}
    public static void putAudio(Audio a){
        audios.put(a.id,a);

        debug("[AudioManager]音效已加载: "+a.name+"["+a.id+"]");
    }

    public static List<AudioState> getRegisterAudios(String world){return registerStates.get(world);}
    public static HashMap<String, List<AudioState>> getRegisterStates(){return registerStates;}
    public static HashMap<String, AudioState> getRegisterStatesID(){return registerStatesID;}
    public static List<AudioState> getRegisterAudios(Audio a){
        List<AudioState> l = new ArrayList<>();
        for(AudioState s : registerStatesID.values())
            if(s.audio.equals(a))
                l.add(s);
        return l;
    }
    public static void registerAudioState(AudioState state){
        String world = state.location.getWorld().getName();
        List<AudioState> list = registerStates.getOrDefault(world,new ArrayList<>());
        list.add(state);
        registerStates.put(world,list);
        registerStatesID.put(state.id,state);

        debug("[AudioManager]成功注册状态: "+state.name+"["+state.id+"]");
    }
    public static void unRegisterAudioStateAll(String world)
    {
        List<AudioState> list = registerStates.get(world);
        if(list!=null){
            list.forEach(e->registerStatesID.remove(e.id));
            list.clear();
        }
        debug("[AudioManager]删除世界["+world+"]全部注册状态");
    }
    public static void unRegisterAudioState(AudioState state)
    {
        String world = state.location.getWorld().getName();
        List<AudioState> list = registerStates.get(world);
        if(list!=null){
            list.removeIf(e->e.equals(state));
            registerStatesID.remove(state.id);
        }

        debug("[AudioManager]删除全部注册状态: "+state.name+"["+state.id+"]");
    }
    public static void unRegisterAudioState(String world , String id)
    {
        List<AudioState> list = registerStates.get(world);
        if(list!=null){
            list.removeIf(e->e.id.equals(id));
            registerStatesID.remove(id);
        }
        debug("[AudioManager]删除世界["+world+"]全部注册状态: "+id);
    }
    public static void unRegisterAudioState(String id)
    {
        for(String world : audios.keySet()){
            List<AudioState> list = registerStates.get(world);
            if(list!=null){
                list.removeIf(e->e.id.equals(id));
                registerStatesID.remove(id);
            }
        }
        debug("[AudioManager]删除全部注册状态: "+id);
    }
    public static AudioState getRegisterState(Location loc)
    {
        return getRegisterState(loc.getWorld().getName(),loc.getX(),loc.getY(),loc.getZ());
    }
    public static AudioState getRegisterState(String id)
    {
        return registerStatesID.get(id);
    }
    public static AudioState getRegisterState(String world , String id)
    {
        List<AudioState> list = getRegisterAudios(world);
        if(list==null)return null;

        for(AudioState s : list)
            if(s.id.equals(id))
                return s;
        return null;
    }
    public static AudioState getRegisterState(String world, double x, double y, double z)
    {
        List<AudioState> list = getRegisterAudios(world);
        if(list==null)return null;

        for(AudioState s : list)
        {
            Location l = s.location;
            if(l.getWorld().getName().equals(world)&&l.getX()==x&&l.getY()==y&&l.getZ()==z)
                return s;
        }

        return null;
    }

    public static HashMap<AudioState,Boolean> getActive(PlayerAudioState ps)
    {
        HashMap<AudioState,Boolean> active = new HashMap<>();
        List<AudioState> list = getRegisterAudios(ps.player.getLocation().getWorld().getName());
        if(list==null)return active;

        for (AudioState s : list)
            if(s.canPlay(ps))   active.put(s,true);
            else                active.put(s,false);
        return active;
    }
    
    public static void debug(String v)
    {
        if(!options.debug)return;
        Bukkit.getLogger().info(v);
    }
}

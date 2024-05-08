package net.mcbbs.a1mercet.environmentalaudio.function.environmental;

import net.mcbbs.a1mercet.environmentalaudio.EnvironmentalAudio;
import net.mcbbs.a1mercet.environmentalaudio.config.IConfig;
import net.mcbbs.a1mercet.environmentalaudio.event.custom.audio.AudioPlayEvent;
import net.mcbbs.a1mercet.environmentalaudio.event.custom.audio.AudioStopEvent;
import net.mcbbs.a1mercet.environmentalaudio.function.AudioType;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.audioevent.IAudioEvent;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.GAudioEffectFactory;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.GAudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.IDebugEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Audio implements IConfig
{
    public interface AudioCallback
    {
        boolean handle(PlayerAudioState ps , AudioState state);
    }

    @Override public String getDefaultPath() {return id;}

    @Override
    public void save(ConfigurationSection section) {
        section.set("",null);
        section.set("Name"      ,name);
        section.set("Type"      ,type.name());
        data.save(section.createSection(data.getDefaultPath()));
        for(IAudioEvent e : playEvent)
            e.save(section.createSection("PlayEvent."+e.getDefaultPath()+"."));
        for(IAudioEvent e : stopEvent)
            e.save(section.createSection("StopEvent."+e.getDefaultPath()+"."));
    }

    @Override
    public void load(ConfigurationSection section) {
        if(section.getConfigurationSection("AudioData")!=null)
            data.load(section.getConfigurationSection("AudioData"));
        else Bukkit.getLogger().severe("[Audio]音效数据未填写(AudioData)");

        if(section.getConfigurationSection("PlayEvent")!=null)
            for(String id : section.getConfigurationSection("PlayEvent").getKeys(false))
            {
                IAudioEvent.EnumAudioEvent type = IAudioEvent.EnumAudioEvent.valueOf(section.getString("PlayEvent."+id+".Type"));
                IAudioEvent event = type.createInstance(id);
                event.load(section.getConfigurationSection("PlayEvent."+id+"."));
                playEvent.add(event);
            }
        if(section.getConfigurationSection("StopEvent")!=null)
            for(String id : section.getConfigurationSection("StopEvent").getKeys(false))
            {
                IAudioEvent.EnumAudioEvent type = IAudioEvent.EnumAudioEvent.valueOf(section.getString("StopEvent."+id+".Type"));
                IAudioEvent event = type.createInstance(id);
                event.load(section.getConfigurationSection("StopEvent."+id+"."));
                stopEvent.add(event);
            }

    }

    public enum Type
    {
        AMB_INDOOR("室内环境"),
        AMB_OUTDOOR("室外环境"),
        EVENT("事件"),
        MISC("杂项"),
        ;
        public final String name;
        Type(String name) {this.name = name;}
        public String getName() {return name;}
    }

    public final AudioType type;
    public final String id;
    public final String name;
    protected boolean allowEnhance  = true;
    public AudioData data           = new AudioData();
    public List<IAudioEvent> playEvent = new ArrayList<>();
    public List<IAudioEvent> stopEvent = new ArrayList<>();

    public AudioCallback callbackCheck;
    public AudioCallback callbackPlay;
    public AudioCallback callbackStop;

    public Audio registerCallbackCheck(AudioCallback c){this.callbackCheck=c;return this;}
    public Audio registerCallbackPlay(AudioCallback c){this.callbackPlay=c;return this;}
    public Audio registerCallbackStop(AudioCallback c){this.callbackStop=c;return this;}

    public Audio( String id, String name){this(AudioType.DEFAULT,id,name);}
    protected Audio(AudioType type , String id, String name)
    {
        this.id = id.replace("germmod:","").replace("dragon:","");
        this.name = name;
        this.data.setSound(id);
        this.type=type;
        AudioManager.putAudio(this);
    }

    public Audio setField(String name , Object o)
    {
        data.setField(name,o);
        return this;
    }
    public Audio addMute(String id)         {data.mute.add(id);return this;}
    public Audio addMute(Type type)         {return addMute(type.name());}
    public Audio addExclude(String id)      {data.exclude.add(id);return this;}
    public Audio addExclude(Type type)      {return addExclude(type.name());}


    public boolean canPlay(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();

        if(callbackCheck!=null&&!callbackCheck.handle(ps, state))return false;

        if(!state.isEnable())
            return false;
        if(data.range > 0 && state.location.distance(ps.player.getLocation()) > data.range)
            return false;

        for(String exclude : data.exclude)
            if(ps.hasExclude(exclude))
                return false;

        return true;
    }

    public boolean play(PlayerAudioState ps, AudioState state)
    {
        if(ps.isPlaying(state))return false;

        if(callbackCheck!=null&&!callbackPlay.handle(ps, state))return false;

        AudioPlayEvent evt = new AudioPlayEvent(ps,state);
        EnvironmentalAudio.getInstance().getServer().getPluginManager().callEvent(evt);
        if(evt.isCancelled())return false;

        boolean cancelled = !state.getData().cycleReset && state.getData().cycleDelay > 0 && ((System.currentTimeMillis() / 50L * 50L / 50L)) % state.getData().cycleDelay != 0;

        AudioManager.debug("[Audio]播放 "+ps.player.getName()+" > "+state.name+"["+state.id+"]");

        AudioData data = state.getData();

        for(String s : data.mute)
            ps.processMute(s);

        if(!cancelled)playBypass(ps,state);
        ps.processPlaying(state);
        state.registerHandler(ps);

        playEvent.forEach(e->e.handle(ps,state));

        return true;
    }
    public void playBypass(PlayerAudioState ps, AudioState state)
    {
        GAudioManager.playSound(ps,state);
    }
    public boolean stop(PlayerAudioState ps, AudioState state)
    {
        if(!ps.has(state))return true;

        if(callbackStop!=null&&!callbackStop.handle(ps, state))return false;

        AudioStopEvent evt = new AudioStopEvent(ps,state);
        EnvironmentalAudio.getInstance().getServer().getPluginManager().callEvent(evt);
        if(evt.isCancelled())return false;

        AudioManager.debug("[Audio]停止 "+ps.player.getName()+" > "+state.name+"["+state.id+"]");

        AudioData data = state.getData();

        for(String s : data.mute)
            ps.removeMute(s);

        stopBypass(ps,state);
        ps.processRemove(state);
        state.removeHandler(ps);

        stopEvent.forEach(e->e.handle(ps,state));

        return true;
    }

    public void stopBypass(PlayerAudioState ps, AudioState state)
    {
        GAudioManager.stopSound(ps,state);
    }

    public HashMap<IDebugEffect,Location> createDebugEffect(AudioState state)
    {
        return GAudioEffectFactory.createEffect(state);
    }


    public AudioState createState(String id, String name, Location loc){return new AudioState(id,name,this,loc);}
    public AudioState createState(String id, String name){return new AudioState(id,name,this,null);}
    public AudioState createState(Location loc){return new AudioState(id,name,this,loc);}
    public boolean isAllowEnhance() {return allowEnhance;}
}

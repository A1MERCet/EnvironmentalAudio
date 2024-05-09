package net.mcbbs.a1mercet.environmentalaudio.function.environmental;

import net.mcbbs.a1mercet.environmentalaudio.config.IConfig;
import net.mcbbs.a1mercet.environmentalaudio.event.EventAudio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class AudioState implements IConfig
{
    @Override public String getDefaultPath() {return id;}

    @Override
    public void save(ConfigurationSection section) {
        section.set("",null);
        section.set("Name"      ,name);
        section.set("Audio"     ,audio.id);
        section.set("Location"  ,location.getWorld().getName()+" "+location.getX()+" "+location.getY()+" "+location.getZ());
        if(data!=null)data.save(section.createSection(data.getDefaultPath()));
    }

    @Override
    public void load(ConfigurationSection section) {
        String[] locationString = section.getString("Location").split(" ");
        location = new Location(Bukkit.getWorld(locationString[0]),Double.parseDouble(locationString[1]),Double.parseDouble(locationString[2]),Double.parseDouble(locationString[3]));
        if(section.getConfigurationSection("AudioData")!=null)
            data.load(section.getConfigurationSection("AudioData"));
    }

    public final String id;
    public final String name;
    public final Audio audio;
    public Location location;
    protected boolean enable = true;
    private AudioData data   = null;
    protected final HashMap<String, PlayerAudioState> handleList = new HashMap<>();
    public HashMap<String, PlayerAudioState> getHandleList(){return handleList;}
    public void removeHandler(String player){handleList.remove(player);}
    public void removeHandler(PlayerAudioState state){handleList.remove(state.player.getName());}
    public void registerHandler(PlayerAudioState state){handleList.put(state.player.getName(),state);}

    public AudioState(String id,String name,Audio audio) {this(id,name,audio,null);}
    public AudioState(String id,String name,Audio audio, Location location)
    {
        this.id         = id;
        this.name       = name;
        this.audio      = audio;
        this.location   = location;
        this.data       = audio.data.copy();
    }

    public AudioState setField(String name , Object o)
    {
        if(data==null)data=audio.data.copy();
        data.setField(name,o);
        return this;
    }

    public AudioState addMute(String id)            {data.mute.add(id);return this;}
    public AudioState addMute(Audio.Type type)      {return addMute(type.name());}
    public AudioState addExclude(String id)         {data.exclude.add(id);return this;}
    public AudioState addExclude(Audio.Type type)   {return addExclude(type.name());}


    public AudioData getData() {return data==null?audio.data:data;}
    public AudioState copy(String id)
    {
        AudioState a = audio.createState(id,name,new Location(location.getWorld(),location.getX(),location.getY(),location.getZ()));
        a.data       = data.copy();
        return a;
    }

    public boolean isEnable() {return enable;}
    public void setEnable(boolean enable) {
        if(!this.enable&&enable){
            handleList.values().forEach(this::playBypass);
            EventAudio.checkPlayers(location.getWorld());
        }else {
            handleList.values().forEach(this::stopBypass);
        }
        this.enable = enable;
    }

    public boolean shouldMute(String muteType)
    {
        return audio.shouldMute(this,muteType);
    }

    public boolean stop       (PlayerAudioState ps)  {return audio.stop(ps,this);}
    public void stopBypass    (PlayerAudioState ps)  {audio.stopBypass(ps,this);}
    public boolean play       (PlayerAudioState ps)  {return audio.play(ps,this);}
    public void playBypass    (PlayerAudioState ps)  {audio.playBypass(ps,this);}
    public boolean canPlay    (PlayerAudioState ps)  {return audio.canPlay(ps,this);}
}

package net.mcbbs.a1mercet.environmentalaudio.function.environmental.type;

import net.mcbbs.a1mercet.environmentalaudio.config.IConfig;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class AudioData implements IConfig {
    public enum PlayType{
        VANILLA(""),
        GERM("germmod:"),
        DRAGON("dragon:"),
        ;
        public final String prev;
        PlayType(String prev) {this.prev = prev;}
    }

    @Override
    public String getDefaultPath() {
        return "AudioData";
    }

    @Override
    public void save(ConfigurationSection section) {
        section.set("Group",        group);
        section.set("Sound",        sound);
        section.set("Category",     category.name());
        section.set("Cycle",        cycle);
        section.set("CycleReset",   cycleReset);
        section.set("CycleDelay",   cycleDelay);
        section.set("Length",       length);
        section.set("Volume",       volume);
        section.set("Pitch",        pitch);
        section.set("Range",        range);
        section.set("FadeOut",      fadeOut);
        section.set("FadeIn",       fadeIn);
        section.set("Enhance",      enhance);
        section.set("Exclude",      exclude);
        section.set("Mute",         mute);
    }

    @Override
    public void load(ConfigurationSection section) {
        group       = section.getString("Group"                         , group);
        sound       = section.getString("Sound"                         , sound);
        category    = SoundCategory.valueOf(section.getString("Category", category.name()));
        length      = section.getLong("Length"                          , length);
        cycle       = section.getInt("Cycle"                            , cycle);
        volume      = (float) section.getDouble("Volume"                , volume);
        pitch       = (float) section.getDouble("Pitch"                 , pitch);
        range       = section.getInt("Range"                            , range);
        fadeOut     = section.getInt("FadeOut"                          , fadeOut);
        fadeIn      = section.getInt("FadeIn"                           , fadeIn);
        cycleDelay  = section.getLong("CycleDelay"                      , cycleDelay);
        cycleReset  = section.getBoolean("CycleReset"                   , cycleReset);
        enhance     = section.getBoolean("Enhance"                      , enhance);
        List<String> excludeS                   = section.getStringList("Exclude");
        List<String> muteS                      = section.getStringList("Mute");
        if(excludeS.size()>0)   this.exclude    = excludeS;
        if(muteS.size()>0)      this.mute       = muteS;
    }

    public PlayType playType        = PlayType.VANILLA;
    public String group             = Audio.Type.MISC.name;
    protected String sound          = "";
    public SoundCategory category   = SoundCategory.MASTER;
    public int cycle                = -1;
    public boolean cycleReset       = false;
    public long cycleDelay          = 0L;
    public long length              = 1L;
    public float volume             = 1F;
    public float pitch              = 1F;
    public int range                = 10000;
    public int fadeIn               = 30;
    public int fadeOut              = 30;
    public boolean enhance          = false;
    public List<String> exclude     = new ArrayList<>();
    public List<String> mute        = new ArrayList<>();

    @Override
    public String toString() {
        return "[" + group + "] " + sound+(playType==PlayType.GERM?"[G]":playType==PlayType.DRAGON?"[D]":"") + " " + ("循环"+ cycle + " " + (cycleReset?"重设":"") + cycleDelay) + (enhance ? "+增强" : "") + " 音量: " + volume + "/" + pitch + " 范围: " + range + " 排除: " + exclude + " 停止: " + mute;
    }

    public AudioData() {

    }

    public AudioData copy() {
        AudioData d     = new AudioData();
        d.playType      = this.playType;
        d.group         = this.group;
        d.sound         = this.sound;
        d.category      = this.category;
        d.cycle         = this.cycle;
        d.cycleDelay    = this.cycleDelay;
        d.cycleReset    = this.cycleReset;
        d.length        = this.length;
        d.volume        = this.volume;
        d.pitch         = this.pitch;
        d.range         = this.range;
        d.fadeIn        = this.fadeIn;
        d.fadeOut       = this.fadeOut;
        d.enhance       = this.enhance;
        d.exclude       = new ArrayList<>(this.exclude);
        d.mute          = new ArrayList<>(this.mute);
        return d;
    }


    public AudioData setField(String name , Object o)
    {
        try {
            this.getClass().getField(name).set(AudioData.this,o);
        }catch (Exception e){e.printStackTrace();}
        return this;
    }

    public AudioData setSound(String sound)
    {
        if(sound.contains(PlayType.GERM.prev)) {
            sound=sound.replace(PlayType.GERM.prev,"");
            playType=PlayType.GERM;
        } else if (sound.contains(PlayType.DRAGON.prev)) {
            sound=sound.replace(PlayType.DRAGON.prev,"");
            playType=PlayType.DRAGON;
        } else {
            playType=PlayType.VANILLA;
        }
        this.sound = sound;
        return this;
    }
    public AudioData setCategory(SoundCategory category)    {this.category = category;return this;}
    public AudioData setCycle(int cycle)                    {this.cycle = cycle;return this;}
    public AudioData setLength(long length)                 {this.length = length;return this;}
    public AudioData setVolume(float volume)                {this.volume = volume;return this;}
    public AudioData setPitch(float pitch)                  {this.pitch = pitch;return this;}
    public AudioData setRange(int range)                    {this.range = range;return this;}
    public AudioData addMute(String id)                     {mute.add(id);return this;}
    public AudioData addMute(Audio.Type t)                  {return addMute(t.name());}
    public AudioData addExclude(String id)                  {exclude.add(id);return this;}
    public AudioData addExclude(Audio.Type t)               {return addExclude(t.name());}
    public AudioData setGroup(String group)                 {this.group = group;return this;}
    public AudioData setType(Audio.Type type)               {return setGroup(type.name());}
    public AudioData setEnhance(boolean enhance)            {this.enhance = enhance;return this;}
    public String getSound()                                {return sound;}
}

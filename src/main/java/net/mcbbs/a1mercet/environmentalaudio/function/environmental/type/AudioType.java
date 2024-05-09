package net.mcbbs.a1mercet.environmentalaudio.function.environmental.type;

import net.mcbbs.a1mercet.environmentalaudio.EnvironmentalAudio;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioType
{
    public static final HashMap<Plugin,Boolean> registerPlugins = new HashMap<>();
    public static final HashMap<String,Class<? extends Audio>> map = new HashMap<>();

    public static Class<? extends Audio> forName(String type){return map.get(type);}
    public static Audio newInstance(String type,String id , String name)
    {
        try {
            Class<? extends Audio> c = forName(type); if(c==null)return null;
            Constructor<? extends Audio> con = c.getConstructor(String.class,String.class);
            return con.newInstance(id,name);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    public static void registerType(Plugin plugin , String str , Class<? extends Audio> audio)
    {
        map.put(str,audio);
        Bukkit.getLogger().warning("成功注册 ["+str+"] 类型音效");
        registerPlugins.put(plugin,false);
    }

    static {
        registerType(EnvironmentalAudio.getInstance(),"DEFAULT",     Audio.class);
        registerType(EnvironmentalAudio.getInstance(),"AREA",        AudioArea.class);
        registerType(EnvironmentalAudio.getInstance(),"ENTITY",      AudioEntity.class);
    }
}

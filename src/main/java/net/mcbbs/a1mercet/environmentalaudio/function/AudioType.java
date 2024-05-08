package net.mcbbs.a1mercet.environmentalaudio.function;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioArea;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class AudioType
{
    public static HashMap<String,Class<? extends Audio>> map = new HashMap<>();
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

    public static void registerType(String str , Class<? extends Audio> audio)
    {
        map.put(str,audio);
        Bukkit.getLogger().warning("成功注册 ["+str+"] 类型音效");
    }

    static {
        registerType("DEFAULT", Audio.class);
        registerType("AREA", AudioArea.class);
    }
}

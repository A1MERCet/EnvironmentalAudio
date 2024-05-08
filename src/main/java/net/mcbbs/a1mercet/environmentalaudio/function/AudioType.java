package net.mcbbs.a1mercet.environmentalaudio.function;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioArea;

public enum AudioType
{
    DEFAULT {public Audio create(String id, String name){return new Audio(id,name);}},
    AREA    {public AudioArea create(String id, String name){return new AudioArea(id,name);}},
    ;

    public Audio create(String id,String name){return new Audio(id,name);}
}

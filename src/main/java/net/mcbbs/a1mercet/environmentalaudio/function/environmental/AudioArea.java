package net.mcbbs.a1mercet.environmentalaudio.function.environmental;

import net.mcbbs.a1mercet.environmentalaudio.function.AudioType;
import net.mcbbs.a1mercet.environmentalaudio.function.area.Area;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class AudioArea extends Audio
{

    @Override
    public void save(ConfigurationSection section) {
        super.save(section);
        section.set("Area",area.id);
    }

    @Override
    public void load(ConfigurationSection section) {
        super.load(section);
        area = AudioManager.getArea(section.getString("Area"));
        if(area==null) {
            Bukkit.getLogger().severe("["+id+"] 加载区域 "+section.getString("Area")+" 不存在");
            area=new Area(id,name);
        }
    }

    public Area area;
    public AudioArea(String id, String name){this(id,name,new Area("nan","NaN"));}
    public AudioArea(String id, String name,Area area)
    {
        super("AREA",id, name);
        this.allowEnhance=false;
        this.area=area;
        data.range=-1;
    }

    public AudioArea setArea(Area area) {this.area = area;return this;}

    @Override
    public boolean canPlay(PlayerAudioState ps, AudioState state) {
        if(!super.canPlay(ps, state))return false;
        return area.inArea(ps.player.getLocation(),state.location.getWorld().getName());
    }
}

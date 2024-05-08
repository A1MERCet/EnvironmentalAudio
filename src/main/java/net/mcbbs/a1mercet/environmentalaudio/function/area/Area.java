package net.mcbbs.a1mercet.environmentalaudio.function.area;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Area
{
    public final String id;
    public final String name;
    public final List<AreaChunk> chunks = new ArrayList<>();

    public Area(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public AreaChunk getArea(Location l){return getArea(l,null);}
    public AreaChunk getArea(Location l,String world)
    {
        for (AreaChunk i : chunks)
            if (i.inArea(l,world)) return i;
        return null;
    }

    public boolean inArea(Location l){return inArea(l,null);}
    public boolean inArea(Location l,String world)
    {
        for (AreaChunk i : chunks)
            if (i.inArea(l,world)) return true;
        return false;
    }

    public AreaChunk getAreaChunk(Location l){return getAreaChunk(l,null);}
    public AreaChunk getAreaChunk(Location l,String world)
    {
        for (AreaChunk i : chunks)
            if (i.inArea(l,world)) return i;
        return null;
    }


    public List<AreaChunk> getChunks() {return chunks;}
    public String getId() {return id;}
    public String getName() {return name;}
    public Area addAreaChunk(AreaChunk c) {this.chunks.add(c);return this;}

    public Area addAreaChunk(String world , double x1, double y1, double z1, double x2, double y2, double z2) {
        this.chunks.add(new AreaChunk(this).setLoc1(world,x1, y1, z1).setLoc2(world,x2, y2, z2));
        return this;
    }
}

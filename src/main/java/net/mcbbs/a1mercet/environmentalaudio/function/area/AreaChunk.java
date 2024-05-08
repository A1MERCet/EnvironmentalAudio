package net.mcbbs.a1mercet.environmentalaudio.function.area;

import net.mcbbs.a1mercet.environmentalaudio.util.PlayerHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class AreaChunk {

    public Area parent;
    public Location l1;
    public Location l2;

    public AreaChunk(Area parent) {
        this.parent=parent;
    }

    public AreaChunk(Area parent,Location l1, Location l2) {
        this.parent=parent;
        this.l1 = l1;
        this.l2 = l2;
    }

    public boolean inArea(Location l){return inArea(l,null);}
    public boolean inArea(Location l , String world) {
        if(world!=null && !(l1.getWorld().getName()).equals(world))return false;
        return PlayerHelper.inAreaIgnoreWorld(l, l1, l2);
    }

    public AreaChunk setLoc1(String world , double x, double y, double z) {
        l1 = new Location(Bukkit.getWorld(world), x, y, z);
        return this;
    }

    public AreaChunk setLoc2(String world , double x, double y, double z) {
        l2 = new Location(Bukkit.getWorld(world), x, y, z);
        return this;
    }
}

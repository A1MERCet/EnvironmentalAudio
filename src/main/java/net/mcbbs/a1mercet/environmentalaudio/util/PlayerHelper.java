package net.mcbbs.a1mercet.environmentalaudio.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerHelper
{

    /**
     * ��ȡһ�������ڵ��������
     */
    public static List<Player> getAllPlayerInArea(Location location , int areaX , int areaY , int areaZ)
    {
        List<Player> ps = new ArrayList<>();
        if(location.getWorld()==null)return ps;
        for(Entity e : location.getWorld().getNearbyEntities(location,areaX,areaY,areaZ))
            if(e instanceof Player)
                ps.add((Player) e);
        return ps;
    }

    /**
     * �ж�һ���������Ƿ������
     */
    public static boolean hasPlayerArea(Location location , int areaX , int areaY , int areaZ)
    {
        return getAllPlayerInArea(location,areaX,areaY,areaZ).size()>0;
    }
    public static boolean hasPlayerArea(Location location , int area)
    {
        return hasPlayerArea(location,area,area,area);
    }

    /**
     * @param p player's location
     * @param l1 location 1
     * @param l2 location 2
     * @return Where the player is between l1 and l2
     */
    public static boolean inAreaIgnoreWorld(Location p, Location l1 , Location l2) {
        int x1 = l1.getBlockX() , y1 = l1.getBlockY() , z1 = l1.getBlockZ();
        int x2 = l2.getBlockX() , y2 = l2.getBlockY() , z2 = l2.getBlockZ();
        int minY = Math.min(y1, y2) - 1;
        int maxY = Math.max(y1, y2) + 1;
        int minZ = Math.min(z1, z2) - 1;
        int maxZ = Math.max(z1, z2) + 1;
        int minX = Math.min(x1, x2) - 1;
        int maxX = Math.max(x1, x2) + 1;
        if (p.getX() > minX && p.getX() < maxX)
            if (p.getY() > minY && p.getY() < maxY)
                return p.getZ() > minZ && p.getZ() < maxZ;
        return false;
    }
}

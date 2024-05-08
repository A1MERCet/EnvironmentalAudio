package net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui;

import com.germ.germplugin.api.GermPacketAPI;
import com.germ.germplugin.api.SoundType;
import com.germ.germplugin.api.dynamic.gui.GermGuiScreen;
import com.germ.germplugin.api.dynamic.gui.GuiManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioData;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GAudioManager
{
    protected static boolean germ = false;
    protected static boolean dragon = false;

    public static boolean isGerm() {return germ;}
    public static void setGerm(boolean germ) {
        GAudioManager.germ = germ;
        if(germ){
            Bukkit.getLogger().info("§6§l[Audio]已启用萌芽支持");
        }
    }
    public static boolean isDragon() {return dragon;}
    public static void setDragon(boolean dragon) {
        GAudioManager.dragon = dragon;
        if(dragon){
            Bukkit.getLogger().info("§6§l[Audio]已启用萌芽支持");
        }
    }

    public static void playSound(PlayerAudioState ps, AudioState state)
    {
        if(state.getData().enhance && state.audio.isAllowEnhance() && state.getData().range>0)
            playSoundVanillaEnhance(ps,state);

        else if(isGerm())   playSoundGerm(ps,state);
        else if(isDragon()) playSoundDragon(ps,state);
        else                playSoundVanilla(ps,state);
    }
    public static void stopSound(PlayerAudioState ps, AudioState state)
    {
        if(isGerm())        stopSoundGerm(ps,state);
        else if(isDragon()) stopSoundDragon(ps,state);
        else                stopSoundVanilla(ps,state);
    }

    protected static void playSoundVanilla(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        ps.player.playSound(ps.player.getLocation(),data.getSound(), data.category,data.volume,data.pitch);
    }
    protected static void playSoundVanillaEnhance(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        Location rec = calculateAdjustedSoundCoordinates(state.location,data.range,ps.player.getLocation(),6D);
        double scale = state.location.distance(ps.player.getLocation())/data.range;

        ps.player.playSound(rec,data.getSound(), data.category, data.range<0?1F:(float) (data.volume*scale),data.pitch);
    }
    protected static void playSoundGerm(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        SoundType type = SoundType.MASTER;
        switch (data.category){
            case MASTER:    type=SoundType.MASTER;  break;
            case NEUTRAL:   type=SoundType.NEUTRAL; break;
            case MUSIC:     type=SoundType.MUSIC;   break;
            case WEATHER:   type=SoundType.WEATHER; break;
            case PLAYERS:   type=SoundType.PLAYER;  break;
            case VOICE:     type=SoundType.VOICE;   break;
            case BLOCKS:    type=SoundType.BLOCKS;  break;
            case AMBIENT:   type=SoundType.AMBIENT; break;
            case HOSTILE:   type=SoundType.HOSTILE; break;
            case RECORDS:   type=SoundType.RECORDS; break;
        }
        GermPacketAPI.playSound(ps.player,data.getSound(), type,0,0,0,0,data.volume,data.pitch,data.cycle);
    }
    protected static void playSoundDragon(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        //todo
    }


    protected static void stopSoundVanilla(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        ps.player.stopSound(data.getSound());
    }
    protected static void stopSoundGerm(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        GermPacketAPI.stopSound(ps.player,data.getSound(),data.fadeOut);
    }
    protected static void stopSoundDragon(PlayerAudioState ps, AudioState state)
    {
        AudioData data = state.getData();
        //todo
    }

    public static void openGui(Player p , String gui , boolean hud)
    {
        if(isGerm())         openGuiGerm(p,gui,hud);
        else if(isDragon())  openGuiDragon(p,gui,hud);
    }
    protected static void openGuiGerm(Player p , String gui , boolean hud)
    {
        GermGuiScreen g = GermGuiScreen.getGermGuiScreen(gui);
        if(hud)g.openHud(p);
        else   g.openGui(p);
    }
    protected static void openGuiDragon(Player p , String gui , boolean hud)
    {
        //todo
    }

    public static Location calculateAdjustedSoundCoordinates(Location targetSoundCoordinates, double maxDistance, Location receiverCoordinates, double vanilla) {
        double distance = targetSoundCoordinates.distance(receiverCoordinates);

        if (distance <= vanilla && distance <= maxDistance)
            return targetSoundCoordinates;

        double scale = Math.min(vanilla, maxDistance) / distance;

        double newX = receiverCoordinates.getX() + (targetSoundCoordinates.getX() - receiverCoordinates.getX()) * scale;
        double newY = receiverCoordinates.getY() + (targetSoundCoordinates.getY() - receiverCoordinates.getY()) * scale;
        double newZ = receiverCoordinates.getZ() + (targetSoundCoordinates.getZ() - receiverCoordinates.getZ()) * scale;

        return new Location(targetSoundCoordinates.getWorld(), newX, newY, newZ);
    }
}

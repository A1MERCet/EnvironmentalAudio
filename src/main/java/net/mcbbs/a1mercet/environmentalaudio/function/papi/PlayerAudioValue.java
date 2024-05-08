package net.mcbbs.a1mercet.environmentalaudio.function.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerAudioValue extends PlaceholderExpansion
{
    @Override public boolean canRegister(){return true;}
    @Override public String getAuthor(){return "A1MERCet";}
    @Override public String getIdentifier(){return "audio";}
    @Override public String getVersion(){return "1.0.0";}

    /**
     * <br>%audio_is_id_(value)%
     * <br>[true/false]
     * <br>获取当前是否播放了指定value的音效ID
     * <br>
     * <br>%audio_is_state_(value)%
     * <br>[true/false]
     * <br>获取当前是否播放了指定value的音效状态ID
     * <br>
     * <br>%audio_is_type_(value)%
     * <br>[true/false]
     * <br>获取当前是否播放了指定value的音效数据type
     * <br>
     * <br>%audio_getaudio_(filed)_(value)%
     * <br>[string]
     * <br>获取指定音效配置
     * <br>
     * <br>%audio_size%
     * <br>[number]
     * <br>获取当前音效播放列表大小
     * <br>
     * <br>%audio_list%
     * <br>[string]
     * <br>获取当前音效播放列表 以'@'分隔:test_audio1@test_audio2@...
     */

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        try {

            String[] args = identifier.split("_");

            if(!player.isOnline())return "";
            Player p = player.getPlayer();
            PlayerAudioState ps = getState(p);

            switch (args[0]){
                case "is":{
                    if(args.length<3)   return "false";
                    switch (args[1]){
                        case "audio":   return String.valueOf(isAudioPlaying(ps,args[2]));
                        case "state":   return String.valueOf(isStatePlaying(ps,args[2]));
                        case "type":    return String.valueOf(isTypePlaying(ps,args[2]));
                    }}
                case "getaudio":{
                    if(args.length<3)   return "";
                    switch (args[1]){
                        case "name":    return getAudioName(args[2]);
                        case "type":    return getAudiType(args[2]);
                        case "range":   return getAudioRange(args[2]);
                        case "sound":   return getAudioSound(args[2]);
                        case "category":return getAudioCategory(args[2]);
                        case "playtype":return getAudioPlayType(args[2]);
                    }}
                case "size":            return String.valueOf(getPlayListSize(ps));
                case "list":            return String.valueOf(getPlayList(ps,"@"));
            }

        }catch (Exception e){e.printStackTrace();}
        return "";
    }

    public String getAudioCategory(String id)
    {
        Audio a = AudioManager.getAudio(id);
        if(a==null)return "";
        return a.data.category.name();
    }

    public String getAudioPlayType(String id)
    {
        Audio a = AudioManager.getAudio(id);
        if(a==null)return "";
        return a.data.playType.name();
    }

    public String getAudioSound(String id)
    {
        Audio a = AudioManager.getAudio(id);
        if(a==null)return "";
        return a.data.playType.prev+""+a.data.getSound();
    }

    public String getAudioRange(String id)
    {
        Audio a = AudioManager.getAudio(id);
        if(a==null)return "";
        return String.valueOf(a.data.range);
    }

    public String getAudioName(String id)
    {
        Audio a = AudioManager.getAudio(id);
        if(a==null)return "";
        return a.name;
    }
    public String getAudiType(String id)
    {
        Audio a = AudioManager.getAudio(id);
        if(a==null)return "";
        return a.type.name();
    }

    public PlayerAudioState getState(Player p){
        PlayerAudioState s = AudioManager.getPlayerState(p.getName());
        if(s==null)s=AudioManager.createPlayerState(p);
        return s;
    }

    public boolean isAudioPlaying(PlayerAudioState ps, String id)
    {
        for(AudioState state : ps.getPlaying().values())
            if(state.audio.id.equals(id))
                return true;
        return false;
    }

    public boolean isTypePlaying(PlayerAudioState ps, String id)
    {
        for(AudioState state : ps.getPlaying().values())
            if(state.audio.data.group.equals(id))
                return true;
        return false;
    }

    public boolean isStatePlaying(PlayerAudioState ps, String id)
    {
        return ps.isPlaying(id);
    }

    public int getPlayListSize(PlayerAudioState ps)
    {
        return ps.getPlaying().keySet().size();
    }

    public String getPlayList(PlayerAudioState ps,String split)
    {
        StringBuilder b = new StringBuilder();
        ps.getPlaying().keySet().forEach(e->b.append(e).append(split));
        return b.toString();
    }
}

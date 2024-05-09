package net.mcbbs.a1mercet.environmentalaudio.command;

import net.mcbbs.a1mercet.environmentalaudio.EnvironmentalAudio;
import net.mcbbs.a1mercet.environmentalaudio.config.AudioLoader;
import net.mcbbs.a1mercet.environmentalaudio.event.custom.audio.AudioReloadEvent;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.*;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.IDebugEffect;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioData;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioEntity;
import net.mcbbs.a1mercet.environmentalaudio.util.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CMDAudio extends CMDBase
{

    public CMDAudio()
    {
        super("audio",CMDAudio.class);
    }

    @CommandArgs(
            describe = "打开音效状态",
            args     = {"state","enable","音效状态ID"} ,
            types    = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING}
    )
    public void enableState(CommandSender sender,String id)
    {
        AudioState state = AudioManager.getRegisterState(id);
        if(state==null){sender.sendMessage("音效状态["+id+"]不存在");return;}
        state.setEnable(true);
    }

    @CommandArgs(
            describe = "关闭音效状态",
            args     = {"state","disable","音效状态ID"} ,
            types    = {ArgType.DEPEND,ArgType.DEPEND,ArgType.STRING}
    )
    public void disableState(CommandSender sender,String id)
    {
        AudioState state = AudioManager.getRegisterState(id);
        if(state==null){sender.sendMessage("音效状态["+id+"]不存在");return;}
        state.setEnable(false);
    }

    @CommandArgs(
            describe = "查看全部音效",
            args     = {"list"} ,
            types    = {ArgType.DEPEND}
    )
    public void listAudios(CommandSender sender)
    {
        sender.sendMessage("[音效列表]");
        if(AudioManager.getAudios().size()==0)sender.sendMessage("空");
        int indedx = 0;
        for(Audio a : AudioManager.getAudios().values())
        {
            sender.sendMessage(indedx+" - "+a.name+"["+a.id+"]");
            indedx++;
        }
    }
    @CommandArgs(
            describe = "查看全部已注册的音效状态",
            args     = {"list","state"} ,
            types    = {ArgType.DEPEND,ArgType.DEPEND}
    )
    public void listStates(CommandSender sender)
    {
        sender.sendMessage("[音效状态列表]");
        for(String world : AudioManager.getRegisterStates().keySet())
        {
            sender.sendMessage("["+world+"]");
            List<AudioState> states = AudioManager.getRegisterStates().get(world);
            if(states==null||states.size()==0)continue;
            for(int i = 0;i<states.size();i++)
            {
                AudioState s = states.get(i);
                sender.sendMessage(i+" - "+s.name+"["+s.id+"]");
            }
        }
    }

    @CommandArgs(
            describe   = "绑定准星实体至音效状态",
            args       = {"bind","状态ID"} ,
            types      = {ArgType.DEPEND,ArgType.STRING},
            playerOnly = true
    )
    public void bind(CommandSender sender,String state)
    {
        AudioState audioState = AudioManager.getRegisterState(state);
        if(audioState==null){sender.sendMessage("音效状态["+state+"]不存在");return;}
        if(!(audioState instanceof AudioEntity.AudioStateEntity)){
            sender.sendMessage("音效状态["+state+"]类型错误["+audioState.audio.type+"]");
            return;
        }
        Entity entity = UtilPlayer.rayTraceEntity((Player)sender,16D);
        if(entity==null) {sender.sendMessage("准星处无实体");return;}

        ((AudioEntity.AudioStateEntity)audioState).setEntity(entity);
        sender.sendMessage("成功绑定实体["+entity.getName()+" "+entity.getUniqueId().toString()+"]至"+audioState.id+"["+audioState.name+"]");
    }

    @CommandArgs(
            describe   = "绑定实体至音效状态",
            args       = {"bind","状态ID","UUID/玩家"} ,
            types      = {ArgType.DEPEND,ArgType.STRING,ArgType.STRING}
    )
    public void bind(CommandSender sender,String state , String id)
    {
        AudioState audioState = AudioManager.getRegisterState(state);
        if(audioState==null){sender.sendMessage("音效状态["+state+"]不存在");return;}
        if(!(audioState instanceof AudioEntity.AudioStateEntity)){
            sender.sendMessage("音效状态["+state+"]类型错误["+audioState.audio.type+"]");
            return;
        }

        Entity entity = Bukkit.getPlayer(id);
        if(entity==null) entity = Bukkit.getEntity(UUID.fromString(id));

        ((AudioEntity.AudioStateEntity)audioState).setEntity(entity);
    }

    @CommandArgs(
            describe   = "取消绑定实体至音效状态",
            args       = {"unbind","状态ID"} ,
            types      = {ArgType.DEPEND,ArgType.STRING}
    )
    public void unbind(CommandSender sender,String state)
    {
        AudioState audioState = AudioManager.getRegisterState(state);
        if(audioState==null){sender.sendMessage("音效状态["+state+"]不存在");return;}
        if(!(audioState instanceof AudioEntity.AudioStateEntity)){
            sender.sendMessage("音效状态["+state+"]类型错误["+audioState.audio.type+"]");
            return;
        }

        ((AudioEntity.AudioStateEntity)audioState).setEntity(null);
    }

    @CommandArgs(
            describe   = "显示全部已注册的音效",
            args       = {"debug"} ,
            types      = {ArgType.DEPEND},
            playerOnly = true
    )
    public void debug(CommandSender sender)
    {
        Player p = (Player) sender;
        for(World w : Bukkit.getWorlds())
        {
            List<AudioState> stateList = AudioManager.getRegisterAudios(w.getName());
            if(stateList!=null)
                for (AudioState s : stateList)
                {
                    if(!s.location.getWorld().equals(p.getWorld()))continue;
                    HashMap<IDebugEffect,Location> effects = s.audio.createDebugEffect(s);
                    effects.forEach((k,v)->k.spawnTo(p,v));
                }
        }

    }



    @CommandArgs(
            describe   = "重新加载配置文件",
            args       = {"reload"} ,
            types      = {ArgType.DEPEND}
    )
    public void reload(CommandSender sender)
    {
        AudioLoader.reload();
        EnvironmentalAudio.getInstance().getServer().getPluginManager().callEvent(new AudioReloadEvent());
    }

    @CommandArgs(
            describe   = "显示已注册的音效",
            args       = {"debug","音效ID"} ,
            types      = {ArgType.DEPEND, ArgType.STRING,},
            playerOnly = true
    )
    public void debug(CommandSender sender,String audio)
    {
        debug(sender,audio,((Player)sender).getWorld().getName());
    }



    @CommandArgs(
            describe   = "显示已注册的音效",
            args       = {"debug","音效ID","世界"} ,
            types      = {ArgType.DEPEND, ArgType.STRING,ArgType.STRING},
            playerOnly = true
    )
    public void debug(CommandSender sender,String audio,String world)
    {
        Audio a = AudioManager.getAudio(audio);
        if(a==null){sender.sendMessage("音效不存在["+audio+"]");return;}

        Player p = (Player) sender;
        List<AudioState> stateList = AudioManager.getRegisterAudios(world);

        for (AudioState s : stateList)
        {
            HashMap<IDebugEffect,Location> effects = s.audio.createDebugEffect(s);
            effects.forEach((k,v)->k.spawnTo(p,v));
        }
    }



    @CommandArgs(
            describe = "查看已注册世界音效",
            args     = {"show"} ,
            types    = {ArgType.DEPEND}
    )
    public void showAll(CommandSender sender)
    {
        for(World w : Bukkit.getWorlds())
            for(Audio a : AudioManager.getAudios().values())
                show(sender,a.id,w.getName());
    }



    @CommandArgs(
            describe = "查看已注册世界音效",
            args     = {"show","音效ID"} ,
            types    = {ArgType.DEPEND, ArgType.STRING}
    )
    public void show(CommandSender sender,String audio)
    {
        Audio a = AudioManager.getAudio(audio);
        if(a==null){sender.sendMessage("音效不存在["+audio+"]");return;}

        List<AudioState> stateList = AudioManager.getRegisterAudios(a);
        if(stateList.size()==0){sender.sendMessage("空");return;}

        StringBuilder b = new StringBuilder();
        for(int i = 0;i<stateList.size();i++)
        {
            AudioState s = stateList.get(i);
            if(!s.audio.id.equals(audio))continue;
            b.append(i).append(" - ").append(s.name).append("[").append(s.id).append("] ").append(getLocationString(s.location)).append("\n");
        }

        sender.sendMessage(b.toString());
    }


    @CommandArgs(
            describe = "拷贝所有世界音效状态至副本世界",
            args     = {"copy","世界","副本世界"} ,
            types    = {ArgType.DEPEND, ArgType.STRING,ArgType.STRING}
    )
    public void copyTo(CommandSender sender,String source,String target)
    {
        World sourceWorld = Bukkit.getWorld(source);
        World targetWorld = Bukkit.getWorld(target);

        if(sourceWorld==null){sender.sendMessage("原始世界不存在["+source+"]");return;}
        if(targetWorld==null){sender.sendMessage("目标世界不存在["+target+"]");return;}

        List<AudioState> states = AudioManager.getRegisterAudios(source);
        if(states.size()>0)
            for(AudioState state : states)
            {
                AudioState copy = state.copy(target+"_"+state.id);
                AudioManager.registerAudioState(copy);
            }
        sender.sendMessage("拷贝世界音效状态["+source+"]至["+target+"]成功");
    }

    @CommandArgs(
            describe = "清除世界全部音效状态",
            args     = {"clear","世界"} ,
            types    = {ArgType.DEPEND, ArgType.STRING}
    )
    public void removeAll(CommandSender sender,String world)
    {
        AudioManager.unRegisterAudioStateAll(world);
        sender.sendMessage("清除世界全部音效状态["+world+"]完成");
    }

    @CommandArgs(
            describe = "查看已注册世界音效",
            args     = {"show","音效ID","世界"} ,
            types    = {ArgType.DEPEND, ArgType.STRING,ArgType.STRING}
    )
    public void show(CommandSender sender,String audio,String world)
    {
        List<AudioState> stateList = AudioManager.getRegisterAudios(world);
        if(stateList==null||stateList.size()==0){return;}

        StringBuilder b = new StringBuilder();
        for(int i = 0;i<stateList.size();i++)
        {
            AudioState s = stateList.get(i);
            if(!s.audio.id.equals(audio))continue;
            b.append(i).append(" - ").append(s.name).append("[").append(s.id).append("] ").append(getLocationString(s.location)).append("\n");
        }

        if(b.length()>0)sender.sendMessage(b.toString());
    }



    public String getLocationString(Location loc)
    {
        return loc.getWorld().getName()+"-"+loc.getX()+" "+loc.getY()+" "+loc.getZ();
    }



    @CommandArgs(
            describe = "查看玩家播放状态",
            args     = {"player","玩家名"} ,
            types    = {ArgType.DEPEND, ArgType.STRING}
    )
    public void checkPlayer(CommandSender sender,String player)
    {
        PlayerAudioState playState = AudioManager.getPlayerState(player);
        if(playState==null){sender.sendMessage("玩家状态不存在["+player+"]");return;}

        StringBuilder builder = new StringBuilder();
        playState.playing.values().forEach(e->builder.append(e.name).append("[").append(e.id).append("]\n"));

        sender.sendMessage(builder.toString());

    }



    @CommandArgs(
            describe = "查看音效配置",
            args     = {"check","音效ID"} ,
            types    = {ArgType.DEPEND, ArgType.STRING}
    )
    public void checkAudio(CommandSender sender,String audio)
    {
        Audio a = AudioManager.getAudio(audio);
        if(a==null){sender.sendMessage("音效不存在["+audio+"]");return;}
        AudioData d = a.data;
        sender.sendMessage(
              "\nID: "+a.id+
                "\n名字: "+a.name+
                "\n音效数据:"+
                "\n"+d.toString()
        );
    }


    @CommandArgs(
            describe    = "在当前位置注册音效至世界",
            args        = {"register","音效ID","保存文件"},
            types       = {ArgType.DEPEND, ArgType.STRING, ArgType.STRING,ArgType.BOOLEAN},
            playerOnly  = true
    )
    public void registerAudio(CommandSender sender , String audio , boolean save)
    {
        Player p = (Player) sender;
        registerAudio(sender,audio,null,null,p.getLocation(),save);
    }




    @CommandArgs(
            describe    = "注册音效至世界",
            args        = {"register","音效ID","世界","X","Y","Z","保存文件"},
            types       = {ArgType.DEPEND, ArgType.STRING, ArgType.STRING, ArgType.STRING, ArgType.FLOAT, ArgType.FLOAT, ArgType.FLOAT,ArgType.BOOLEAN}
    )
    public void registerAudio(CommandSender sender , String audio , String world , float x, float y , float z,boolean save)
    {
        World bukkitWorld = Bukkit.getWorld(world);
        if(bukkitWorld==null){sender.sendMessage("世界不存在["+world+"]");return;}
        registerAudio(sender,audio,null,null,new Location(bukkitWorld,x,y,z),save);
    }




    @CommandArgs(
            describe    = "注册音效至世界",
            args        = {"register","音效ID","创建ID","创建名","世界","X","Y","Z","保存文件"},
            types       = {ArgType.DEPEND, ArgType.STRING, ArgType.STRING, ArgType.STRING, ArgType.STRING, ArgType.STRING, ArgType.FLOAT, ArgType.FLOAT, ArgType.FLOAT,ArgType.BOOLEAN}
    )
    public void registerAudio(CommandSender sender , String audio , String id , String name , String world , float x, float y , float z,boolean save)
    {
        World bukkitWorld = Bukkit.getWorld(world);
        if(bukkitWorld==null){sender.sendMessage("世界不存在["+world+"]");return;}
        registerAudio(sender,audio,id,name,new Location(bukkitWorld,x,y,z),save);
    }
    

    public void registerAudio(CommandSender sender , String audio , String id , String name, Location location , boolean save)
    {
        if(location.getWorld()==null){sender.sendMessage("世界不存在[null]");return;}
        Audio a = AudioManager.getAudio(audio);
        if(a==null){sender.sendMessage("音效不存在["+audio+"]");return;}

        AudioState source = AudioManager.getRegisterState(location.getWorld().getName(),id);
        if(source!=null){
            sender.sendMessage("注册失败：ID已被占用(音效状态)");
            return;
        }

        AudioState state = a.createState(id==null?a.id:id,name==null?a.name:name,location);
        AudioManager.registerAudioState(state);
        if(save){
            AudioLoader.save("custom",state);
            sender.sendMessage("已注册并存储至[custom.yml]中");
        }else {
            sender.sendMessage("已注册");
        }
    }



    @CommandArgs(
            describe    = "删除音效状态",
            args        = {"remove","创建ID"},
            types       = {ArgType.DEPEND, ArgType.STRING},
            playerOnly  = true
    )
    public void unRegisterAudio(CommandSender sender , String id)
    {
        unRegisterAudio(sender,((Player)sender).getWorld().getName(),id);
    }




    @CommandArgs(
            describe    = "删除音效状态",
            args        = {"remove","世界","创建ID"},
            types       = {ArgType.DEPEND, ArgType.STRING, ArgType.STRING},
            playerOnly  = true
    )
    public void unRegisterAudio(CommandSender sender , String world , String id)
    {
        if(AudioManager.getRegisterAudios(world)==null){sender.sendMessage("世界不存在[null]");return;}
        AudioState source = AudioManager.getRegisterState(world,id);
        if(source==null){sender.sendMessage("音效状态不存在["+id+"]");return;}
        AudioManager.unRegisterAudioState(id);
    }
}

package example;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioData;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Example
{
    public static Audio audio;

    public static void example()
    {

        //创建音效
        //创建自动添加到全局音效库里(AudioManager.audios)
        audio = new Audio("example","实例音效");

        //可设置回调
        audio.registerCallbackCheck((p,s)->{return true;});
        audio.registerCallbackStop((p,s)-> {return true;});
        audio.registerCallbackPlay((p,s)-> {return true;});

        //通过音效数据修改内容
        //详细请看配置文件或者内部定义
        AudioData audioData = audio.data;
        //设置音效请用AudioData.setSound(String)
        //# 支持 萌芽音效 龙核音效
        //# 原版音效minecraft:xxx或modid:xxx
        //# 萌芽音效加前缀"germmod:"
        //# 龙核音效加前缀"dragon:"
        audioData.setSound("germmod:xxx");


        //以音效可创建多个音效状态
        //id name data默认为音效的配置
        AudioState state = audio.createState(new Location(Bukkit.getWorld("world"),0,0,0));

        //音效状态生效需到AudioManager注册
        AudioManager.registerAudioState(state);

        //可随时设置音效是否开启
        state.setEnable(false);


    }
}

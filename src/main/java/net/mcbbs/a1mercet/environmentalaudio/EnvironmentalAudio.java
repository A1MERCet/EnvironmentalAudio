package net.mcbbs.a1mercet.environmentalaudio;

import net.mcbbs.a1mercet.environmentalaudio.command.CMDAudio;
import net.mcbbs.a1mercet.environmentalaudio.config.AudioLoader;
import net.mcbbs.a1mercet.environmentalaudio.event.EventAudio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.papi.PlayerAudioValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class EnvironmentalAudio extends JavaPlugin {

    static EnvironmentalAudio instance;
    public static EnvironmentalAudio getInstance(){return instance;}

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        instance=this;

        saveDefaultConfig();
        AudioManager.options.saveDefaultConfig();

        new PlayerAudioValue().register();

        getServer().getPluginManager().registerEvents(new EventAudio(), this);

        Bukkit.getPluginCommand("audio").setExecutor(new CMDAudio());

        for(Player p : Bukkit.getOnlinePlayers())
            EventAudio.onJoinHandle(p);
    }

    @Override
    public void onDisable()
    {
        for(PlayerAudioState s : AudioManager.getPlayerStates().values())
            new ArrayList<>(s.playing.values()).forEach(e->e.stop(s));
        if(AudioManager.options.tickHandler!=null)
            AudioManager.options.tickHandler.cancel();
    }
}

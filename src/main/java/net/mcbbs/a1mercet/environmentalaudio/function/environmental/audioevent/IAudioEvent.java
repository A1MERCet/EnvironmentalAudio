package net.mcbbs.a1mercet.environmentalaudio.function.environmental.audioevent;

import net.mcbbs.a1mercet.environmentalaudio.config.IConfig;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import org.bukkit.configuration.ConfigurationSection;

public interface IAudioEvent extends IConfig
{

    enum EnumAudioEvent
    {
        COMMAND("Command"){@Override public IAudioEvent createInstance(String id) {return new AEventCommand(id,this);}},
        ;
        public final String id;
        EnumAudioEvent(String id) {this.id = id;}
        public IAudioEvent createInstance(String id){return null;}
    }

    default boolean handle(PlayerAudioState ps , AudioState state){return true;}

    @Override
    default String getDefaultPath(){return getID();}

    @Override
    default void save(ConfigurationSection section)
    {
        section.set("Type",getType().name());
    }

    @Override
    default void load(ConfigurationSection section){}

    String getID();
    EnumAudioEvent getType();
}

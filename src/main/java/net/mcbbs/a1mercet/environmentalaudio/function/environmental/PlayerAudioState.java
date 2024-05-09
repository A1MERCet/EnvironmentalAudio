package net.mcbbs.a1mercet.environmentalaudio.function.environmental;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerAudioState
{

    public final Player player;
    public long moveTick                                    = 0L;
    public long moveTickRound                               = 0L;
    public final HashMap<String, AudioState> playing        = new HashMap<>();
    public final HashMap<String, AudioState> cycle          = new HashMap<>();
    public final HashMap<String,Integer>     cycleRounds    = new HashMap<>();


    public PlayerAudioState(Player player) {
        this.player = player;
    }

    public void clearAll()
    {
        for(AudioState state : new ArrayList<>(playing.values()))
            state.stop(this);
    }

    public void processCycleRounds(String id , int v)
    {
        cycleRounds.put(id, cycleRounds.getOrDefault(id,0)+v);
    }

    public void processPlaying(AudioState state) {
        playing.put(state.id, state);
        if(state.getData().cycle!=0 && (state.getData().cycleDelay>0 || state.getData().enhance))
            cycle.put(state.id,state);
    }

    public void processRemove(AudioState state) {
        playing.remove(state.id);
        cycle.remove(state.id);
    }

    public boolean hasExclude(String excludeType) {
        for (AudioState s : playing.values())
            if (s.id.equals(excludeType)||s.audio.id.equals(excludeType)||s.getData().group.equals(excludeType)||s.audio.type.equals(excludeType))
                return true;
        return false;
    }

    public boolean has(AudioState state)            {return playing.containsKey(state.id);}
    public boolean has(String id)                   {return playing.containsKey(id);}
    public boolean isPlaying(AudioState state)      {return playing.containsKey(state.id);}
    public boolean isPlaying(String id)             {return playing.containsKey(id);}
    public boolean play(AudioState state)           {return state.play(this);}
    public boolean stop(AudioState state)           {return state.stop(this);}
    public boolean stop(String id)                  {AudioState s = playing.get(id);if (s != null) return s.stop(this);return true;}
}

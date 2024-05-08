package net.mcbbs.a1mercet.environmentalaudio.function.environmental;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerAudioState
{

    public final Player player;
    public long moveTick                                    = 0L;
    public long moveTickRound                               = 0L;
    protected final HashMap<String, AudioState> playing     = new HashMap<>();
    public final HashMap<String, AudioState> cycle          = new HashMap<>();

    public PlayerAudioState(Player player) {
        this.player = player;
    }

    public void clearAll()
    {
        for(AudioState state : new ArrayList<>(playing.values()))
            state.stop(this);
    }

    public void processPlaying(AudioState state) {
        playing.put(state.id, state);
        if(state.getData().cycle && (state.getData().cycleDelay>0 || state.getData().enhance)) cycle.put(state.id,state);
    }

    public void processRemove(AudioState state) {
        playing.remove(state.id);
        cycle.remove(state.id);
    }

    public void processMute(String id) {
        List<AudioState> states = new ArrayList<>();
        for (AudioState s : playing.values())
            if (s.id.equals(id)||s.audio.id.equals(id)||s.getData().group.equals(id))
                states.add(s);
        states.forEach(e->e.stopBypass(this));
    }

    public void removeMute(String id) {

        List<AudioState> states = new ArrayList<>();
        for (AudioState s : playing.values())
            if (s.id.equals(id)||s.audio.id.equals(id)||s.getData().group.equals(id))
                states.add(s);
        states.forEach(e->e.playBypass(this));
    }

    public boolean hasExclude(String id) {
        for (AudioState s : playing.values())
            if (s.id.equals(id)||s.audio.id.equals(id)||s.getData().group.equals(id))
                return true;
        return false;
    }

    public boolean has(AudioState state)            {return playing.containsKey(state.id);}
    public boolean has(String id)                   {return playing.containsKey(id);}
    public boolean isPlaying(AudioState state)      {return playing.containsKey(state.id);}
    public boolean isPlaying(String id)             {return playing.containsKey(id);}
    public boolean play(AudioState state)           {return state.play(this);}
    public boolean stop(AudioState state)           {return state.stop(this);}
    public boolean stop(String id)                  {
        AudioState s = playing.get(id);if (s != null) return s.stop(this);return true;}
    public HashMap<String, AudioState> getPlaying() {return playing;}
}

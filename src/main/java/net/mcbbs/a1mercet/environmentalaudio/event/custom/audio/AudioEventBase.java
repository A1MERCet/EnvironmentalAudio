package net.mcbbs.a1mercet.environmentalaudio.event.custom.audio;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;

public class AudioEventBase extends PlayerEventBase
{
    public final PlayerAudioState playState;
    public AudioState audio;

    public AudioEventBase(PlayerAudioState playState, AudioState audio)
    {
        super(playState.player);
        this.playState=playState;
        this.audio=audio;
    }
}

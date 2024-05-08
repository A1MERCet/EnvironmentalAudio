package net.mcbbs.a1mercet.environmentalaudio.event.custom.audio;


import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;

public class AudioPlayEvent extends AudioEventBase
{
    public AudioPlayEvent(PlayerAudioState playState, AudioState audio) {
        super(playState, audio);
    }
}

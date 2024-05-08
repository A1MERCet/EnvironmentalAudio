package net.mcbbs.a1mercet.environmentalaudio.function.environmental.audioevent;

public class AEventBase implements IAudioEvent
{

    public final String id;
    public final EnumAudioEvent type;

    public AEventBase(String id, EnumAudioEvent type) {
        this.id = id;
        this.type = type;
    }

    @Override public String getID() {return id;}
    @Override public EnumAudioEvent getType() {return type;}
}

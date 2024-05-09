package net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui;

import net.mcbbs.a1mercet.environmentalaudio.function.area.Area;
import net.mcbbs.a1mercet.environmentalaudio.function.area.AreaChunk;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioArea;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.type.AudioData;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ.GAudioGermEffectEntity;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ.GAudioGermEffectGui;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ.GAudioGermStateGui;
import net.mcbbs.a1mercet.environmentalaudio.util.UtilDate;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class GAudioEffectFactory
{

    public static HashMap<IDebugEffect, Location> createEffect(AudioState state)
    {
        HashMap<IDebugEffect,Location> map = new HashMap<>();
        if(GAudioManager.isGerm())          map.putAll(createEffectGerm(state));
        else if(GAudioManager.isDragon())   map.putAll(createEffectGerm(state));
        else                                map.putAll(createEffectVanilla(state));

        return map;
    }

    protected static HashMap<IDebugEffect,Location> createEffectVanilla(AudioState state)
    {
        return new HashMap<>();
    }
    protected static HashMap<IDebugEffect,Location> createEffectGerm(AudioState state)
    {
        HashMap<IDebugEffect,Location> map = new HashMap<>();

        AudioData data = state.getData();

        if("AREA".equals(state.audio.type)){

            Area area = ((AudioArea) state.audio).area;
            if(area==null)return map;

            for(int i = 0;i<area.chunks.size();i++)
            {
                AreaChunk chunk = area.chunks.get(i);

                map.put((IDebugEffect) new GAudioGermEffectGui(UUID.randomUUID().toString())
                                .setScale(0.125F).setFollowPitch(true).setFollowYaw(true).setTopRendering(true)
                                .setGui(new GAudioGermStateGui(state))
                                .setRenderRange(1000F)
                                .setDuration(UtilDate.toMinute(1)+"")
                        ,chunk.l1);
                map.put((IDebugEffect) new GAudioGermEffectGui(UUID.randomUUID().toString())
                                .setScale(0.125F).setFollowPitch(true).setFollowYaw(true).setTopRendering(true)
                                .setGui(new GAudioGermStateGui(state))
                                .setRenderRange(1000F)
                                .setDuration(UtilDate.toMinute(1)+"")
                        ,chunk.l2);
                map.put((IDebugEffect) new GAudioGermEffectEntity(UUID.randomUUID().toString())
                        .setYaw("0").setPitch("0").setRoll("0")
                        .setModel("pig").setName("audio_box").setDuration(UtilDate.toMinute(1)+"")
                        .setFollowPitch(false).setFollowYaw(false)
                        .setFollowBindX(false).setFollowBindY(false).setFollowBindZ(false)
                        .setRenderRange(1000F),chunk.l1);
                map.put((IDebugEffect) new GAudioGermEffectEntity(UUID.randomUUID().toString())
                        .setYaw("0").setPitch("0").setRoll("0")
                        .setModel("pig").setName("audio_box").setDuration(UtilDate.toMinute(1)+"")
                        .setFollowPitch(false).setFollowYaw(false)
                        .setFollowBindX(false).setFollowBindY(false).setFollowBindZ(false)
                        .setRenderRange(1000F),chunk.l2)
                ;
            }

        }else {
            map.put((IDebugEffect) new GAudioGermEffectGui(UUID.randomUUID().toString())
                            .setScale(0.125F*data.range).setFollowPitch(true).setFollowYaw(true).setTopRendering(true)
                            .setOffsetX("0.5").setOffsetY("0").setOffsetZ("0.5")
                            .setBindEntityFirstPersonVisible(true)
                            .setYaw("0").setPitch("0").setRoll("0")
                            .setGui(new GAudioGermStateGui(state))
                            .setRenderRange(1000F)
                            .setDuration(UtilDate.toMinute(1)+"")
                    ,state.location);
        }


        return map;
    }
    protected static HashMap<IDebugEffect,Location> createEffectDragon(AudioState state)
    {
        HashMap<IDebugEffect,Location> map = new HashMap<>();
        return map;
    }
}

package net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ;

import com.germ.germplugin.api.dynamic.effect.GermEffectEntity;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.IDebugEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GAudioGermStateBox extends GermEffectEntity implements IDebugEffect {
    public final AudioState state;

    public GAudioGermStateBox(AudioState state) {
        super(UUID.randomUUID().toString());
        this.state = state;
        init();
    }

    protected void init() {
        setModel("pig").setName("audio_box");
        setFollowPitch(false).setFollowYaw(false);
        setFollowBindX(false).setFollowBindY(false).setFollowBindZ(false);
        setYaw("0").setPitch("0").setRoll("0");
        setRenderRange(1000F);
    }

    @Override public void spawnTo(Player p, Location l) {spawnToLocation(p,l);}
}

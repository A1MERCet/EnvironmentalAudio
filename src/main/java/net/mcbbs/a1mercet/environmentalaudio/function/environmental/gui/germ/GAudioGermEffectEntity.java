package net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ;

import com.germ.germplugin.api.dynamic.effect.GermEffectEntity;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.IDebugEffect;
import net.mcbbs.a1mercet.environmentalaudio.util.UtilDate;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GAudioGermEffectEntity extends GermEffectEntity implements IDebugEffect
{

    public GAudioGermEffectEntity(String effectName) {
        super(effectName);
    }

    @Override public void spawnTo(Player p, Location l) {spawnToLocation(p,l);}
}

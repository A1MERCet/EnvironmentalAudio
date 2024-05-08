package net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.germ;

import com.germ.germplugin.api.dynamic.effect.GermEffectGui;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.IDebugEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GAudioGermEffectGui extends GermEffectGui implements IDebugEffect
{
    public GAudioGermEffectGui(String effectName)
    {
        super(effectName);
    }

    @Override public void spawnTo(Player p, Location l) {spawnToLocation(p,l);}
}

package net.mcbbs.a1mercet.environmentalaudio.function.environmental.audioevent;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.GAudioManager;
import org.bukkit.configuration.ConfigurationSection;

public class AEventOpenGui extends AEventBase
{
    public AEventOpenGui(String id, EnumAudioEvent type)
    {
        super(id, type);
    }

    @Override public String getDefaultPath() {return "Gui";}

    @Override
    public void save(ConfigurationSection section)
    {
        super.save(section);
        section.set("Gui",gui);
        section.set("AsHUD",hud);
    }

    @Override
    public void load(ConfigurationSection section)
    {
        gui         = section.getString("Gui");
        hud         = section.getBoolean("AsHUD");
    }

    public String gui;
    public boolean hud;

    @Override
    public boolean handle(PlayerAudioState ps, AudioState state)
    {
        if(!super.handle(ps, state))return false;
        GAudioManager.openGui(ps.player, gui,hud);
        return true;
    }
}

package net.mcbbs.a1mercet.environmentalaudio.function.environmental.audioevent;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class AEventCommand extends AEventBase
{
    public AEventCommand(String id, EnumAudioEvent type) {
        super(id, type);
    }

    @Override public String getDefaultPath() {return "Command";}

    @Override
    public void save(ConfigurationSection section)
    {
        super.save(section);
        section.set("Cmd",cmd);
        section.set("AsOp",asOP);
        section.set("AsConsole",asConsole);
    }

    @Override
    public void load(ConfigurationSection section)
    {
        cmd         = section.getString("Cmd");
        asOP        = section.getBoolean("AsOp");
        asConsole   = section.getBoolean("AsConsole");
    }

    public String cmd;
    public boolean asOP = true;
    public boolean asConsole = true;

    @Override
    public boolean handle(PlayerAudioState ps, AudioState state) {
        if(!super.handle(ps, state))return false;

        Player p = ps.player;
        String cmd = this.cmd
                .replace("%player%",p.getName())
                .replace("%state_id%",state.id)
                .replace("%state_name%",state.name)
                ;

        if(asConsole){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),cmd);
        }else {
            if(asOP && !p.isOp()){
                p.setOp(true);
                try {
                    Bukkit.getServer().dispatchCommand(p,cmd);
                }catch (Exception e){e.printStackTrace();}
                p.setOp(false);
            }else {
                Bukkit.getServer().dispatchCommand(p,cmd);
            }
        }

        return true;
    }
}

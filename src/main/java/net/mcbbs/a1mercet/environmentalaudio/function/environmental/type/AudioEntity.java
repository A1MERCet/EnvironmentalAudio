package net.mcbbs.a1mercet.environmentalaudio.function.environmental.type;

import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AudioEntity extends Audio
{

    public static class AudioStateEntity extends AudioState
    {

        @Override
        public void save(ConfigurationSection section)
        {
            super.save(section);
            section.set("Entity",this.entity);
        }

        @Override
        public void load(ConfigurationSection section)
        {
            super.load(section);
            this.entity=section.getString("Entity");
        }

        public final Location defaultLocation = new Location(Bukkit.getWorld("world"),0,0,0);
        public final AudioEntity audio;
        public String entity;


        public AudioStateEntity(String id, String name, Audio audio , Location loc)
        {
            super(id, name, audio, loc);
            this.audio      = (AudioEntity) audio;
            this.entity     = this.audio.entity;
        }

        public AudioStateEntity resetLocation()
        {
            Entity entity       = getEntity();
            World sourceWorld   = this.location==null?null:this.location.getWorld();
            World newWorld      = entity==null?defaultLocation.getWorld():entity.getLocation().getWorld();

            if(sourceWorld!=null && !sourceWorld.equals(newWorld))
            {
                AudioManager.unRegisterAudioState(this);
                AudioManager.registerAudioState(this);
            }

            this.location=entity==null?defaultLocation:entity.getLocation();
            return this;
        }


        public Entity getEntity()
        {
            if(entity==null)return null;
            Player p = Bukkit.getPlayer(entity);
            if(p!=null) return p;
            else        return Bukkit.getEntity(UUID.fromString(entity));
        }

        public AudioStateEntity setEntity(Entity e)
        {
            this.entity = e==null?null:e instanceof Player ? "player@"+e.getName() : e.getUniqueId().toString();
            resetLocation();
            return this;
        }
    }


    public String entity;


    public AudioEntity(String id, String name)
    {
        super("ENTITY", id, name);
    }

    public AudioEntity setEntity(String e)      {this.entity=e;return this;}

    public AudioEntity setEntity(Entity e)
    {
        this.entity = e==null?null:e instanceof Player ? "player@"+e.getName() : e.getUniqueId().toString();
        return this;
    }

    @Override public AudioState createState(String id, String name, Location loc) {return new AudioStateEntity(id, name, this , loc);}

}

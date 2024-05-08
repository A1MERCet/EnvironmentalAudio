package net.mcbbs.a1mercet.environmentalaudio.config;

import net.mcbbs.a1mercet.environmentalaudio.EnvironmentalAudio;
import net.mcbbs.a1mercet.environmentalaudio.function.AudioType;
import net.mcbbs.a1mercet.environmentalaudio.function.area.Area;
import net.mcbbs.a1mercet.environmentalaudio.function.area.AreaChunk;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.Audio;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioManager;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.AudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.PlayerAudioState;
import net.mcbbs.a1mercet.environmentalaudio.function.environmental.gui.GAudioManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AudioLoader
{

    public static File   baseFile;
    public static File[] typeFileAry;
    public static File[] stateFileAry;
    public static File[] areaFileAry;

    protected static void initPath()
    {
        AudioManager.AudioOptions options = AudioManager.options;

        FileConfiguration localFile = EnvironmentalAudio.getInstance().getConfig();
        String globalPath           = localFile.getString("ConfigPath");
        options.path                = (globalPath==null||"".equals(globalPath))?EnvironmentalAudio.getInstance().getDataFolder().getAbsolutePath()+"/":globalPath;

        baseFile                    = new File(options.path,"AudioConfig.yml");

        File areaFile               = new File(options.path+"areas");
        if(areaFile.isDirectory())  areaFileAry = areaFile.listFiles();

        File typeFile               = new File(options.path+"audios");
        if(typeFile.isDirectory()) typeFileAry = typeFile.listFiles();

        File stateFile              = new File(options.path+"states");
        if(stateFile.isDirectory()) stateFileAry = stateFile.listFiles();
    }
    public static void reload()
    {
        clear();
        initPath();

        AudioManager.AudioOptions options = AudioManager.options;
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(baseFile);

        options.debug           = cfg.getBoolean("Debug",true);
        options.threshold       = cfg.getInt("Threshold",1);
        options.enableTickCheck = cfg.getBoolean("EnableTickCheck",true);
        options.enableFileCheck = cfg.getBoolean("EnableFileCheck",true);
        options.lastReload      = System.currentTimeMillis();
        options.registerFile.clear();

        GAudioManager.setGerm(Bukkit.getPluginManager().isPluginEnabled("GermPlugin"));
        GAudioManager.setDragon(Bukkit.getPluginManager().isPluginEnabled("DragonCore"));
        if(!GAudioManager.isDragon()&&!GAudioManager.isGerm())
            Bukkit.getLogger().info("§6§l[Audio]无萌芽与龙核支持 使用原版兼容");

        AudioManager.init();
        reloadArea();
        reloadType();
        reloadState();


        if(typeFileAry!=null)   options.registerFile.addAll(Arrays.asList(typeFileAry));
        if(stateFileAry!=null)  options.registerFile.addAll(Arrays.asList(stateFileAry));
        if(areaFileAry!=null)   options.registerFile.addAll(Arrays.asList(areaFileAry));
        if(baseFile!=null)      options.registerFile.add(baseFile);
        options.registerFile.add(EnvironmentalAudio.getInstance().getDataFolder());
    }

    public static void save(String path , AudioState state)
    {
        AudioManager.AudioOptions options = AudioManager.options;
        File file = new File((options.path==null?EnvironmentalAudio.getInstance().getDataFolder().getName():options.path)+"/audios/",path+".yml");
        YamlConfiguration cfg =YamlConfiguration.loadConfiguration(file);

        ConfigurationSection section = cfg.getConfigurationSection("AudioState."+state.getDefaultPath());

        state.save(section==null?cfg.createSection("AudioState."+state.getDefaultPath()):section);

        try {cfg.save(file);}catch (Exception e){e.printStackTrace();}
    }

    protected static void reloadArea()
    {
        if(areaFileAry==null){Bukkit.getLogger().severe("[AudioLoader]无Area配置");return;}
        for(File file : areaFileAry)
        {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if(cfg.getConfigurationSection("Area")!=null)
                for(String id: cfg.getConfigurationSection("Area").getKeys(false))
                {
                    try {
                        String name = cfg.getString("Area."+id+".Name");
                        String worldStr = cfg.getString("Area."+id+".World");
                        World world = worldStr==null?null:Bukkit.getWorld(worldStr);

                        if(world==null){Bukkit.getLogger().severe("[AudioLoader]加载区域配置["+id+"]失败 世界["+cfg.getString("Area."+id+".World")+"不存在");continue;}

                        Area area = new Area(id,name);
                        List<String> locList = cfg.getStringList("Area."+id+".Chunks");
                        for(String str : locList)
                        {
                            String[] split = str.split(" ");
                            if(split.length!=6){Bukkit.getLogger().warning("[AudioLoader]加载区域配置["+id+"]出错 Location参数长度出错["+ Arrays.toString(split) +"]");continue;}
                            Location l1 = new Location(world,Double.parseDouble(split[0]),Double.parseDouble(split[1]),Double.parseDouble(split[2]));
                            Location l2 = new Location(world,Double.parseDouble(split[3]),Double.parseDouble(split[4]),Double.parseDouble(split[5]));
                            area.addAreaChunk(new AreaChunk(area,l1,l2));
                        }
                        AudioManager.putArea(area);
                    }catch (Exception e){e.printStackTrace();Bukkit.getLogger().severe("Area["+id+"]加载出错");}
                }
        }

    }

    protected static void reloadType()
    {
        if(typeFileAry==null){Bukkit.getLogger().severe("[AudioLoader]无Audio配置");return;}
        for(File file : typeFileAry)
        {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if(cfg.getConfigurationSection("Audio")!=null)
                for(String id: cfg.getConfigurationSection("Audio").getKeys(false))
                {
                    try {
                        String name = cfg.getString("Audio."+id+".Name");

                        Audio a = AudioType.newInstance(cfg.getString("Audio."+id+".Type") , id , name);
                        if(a==null){Bukkit.getLogger().severe("[AudioLoader]无["+cfg.getString("Audio."+id+".Type")+"]类型音效");continue;}

                        a.load(cfg.getConfigurationSection("Audio."+id));
                    }catch (Exception e){e.printStackTrace();Bukkit.getLogger().severe("Audio["+id+"]加载出错");}
                }
        }
    }

    protected static void reloadState()
    {
        if(stateFileAry==null){Bukkit.getLogger().severe("[AudioLoader]无AudioState配置");return;}
        for(File file : stateFileAry)
        {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            if(cfg.getConfigurationSection("AudioState")!=null)
                for(String id: cfg.getConfigurationSection("AudioState").getKeys(false))
                {
                    try {
                        Audio audio = AudioManager.getAudio(cfg.getString("AudioState."+id+".Audio"));
                        if(audio==null){Bukkit.getLogger().severe("[AudioLoader]["+id+"] 音效ID "+cfg.getString("AudioState."+id+".Audio")+" 不存在");continue;}

                        String name = cfg.getString("AudioState."+id+".Name");

                        AudioState state = audio.createState(id,name==null?audio.name:name);
                        state.load(cfg.getConfigurationSection("AudioState."+id));
                        AudioManager.registerAudioState(state);
                    }catch (Exception e){e.printStackTrace();Bukkit.getLogger().severe("AudioState["+id+"]加载出错");}
                }
        }
    }

    public static void clear()
    {
        for(PlayerAudioState state : AudioManager.getPlayerStates().values())
            state.clearAll();
        AudioManager.getRegisterStates().clear();
        AudioManager.getAudios().clear();
    }
}

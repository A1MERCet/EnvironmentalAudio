package net.mcbbs.a1mercet.environmentalaudio.config;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfig
{
    String getDefaultPath();
    void save(ConfigurationSection section);
    void load(ConfigurationSection section);
}

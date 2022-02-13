package com.darksoldier1404.dsp;

import com.darksoldier1404.dsp.commands.DSPCommand;
import com.darksoldier1404.dsp.events.DSPEvent;
import com.darksoldier1404.dppc.DPPCore;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("all")
public class SimplePrefix extends JavaPlugin {
    public DPPCore core;
    private static SimplePrefix plugin;
    public YamlConfiguration config;
    public YamlConfiguration defaultData = new YamlConfiguration();
    public Map<UUID, YamlConfiguration> udata = new HashMap<>();
    public String prefix;

    public static SimplePrefix getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Plugin pl = getServer().getPluginManager().getPlugin("DPP-Core");
        if(pl == null) {
            getLogger().warning("DPP-Core 플러그인이 설치되어있지 않습니다.");
            getLogger().warning("DP-SimplePrefix 플러그인을 비활성화 합니다.");
            plugin.setEnabled(false);
            return;
        }
        core = (DPPCore) pl;
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("Settings.prefix"));

        plugin.getServer().getPluginManager().registerEvents(new DSPEvent(), plugin);
        getCommand("칭호").setExecutor(new DSPCommand());
    }

    public void onDisable() {
    }
}

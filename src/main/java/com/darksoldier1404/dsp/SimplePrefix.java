package com.darksoldier1404.dsp;

import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dsp.commands.DSPCommand;
import com.darksoldier1404.dsp.events.DSPEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("all")
public class SimplePrefix extends JavaPlugin {
    private static SimplePrefix plugin;
    public static YamlConfiguration config;
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
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        prefix = ColorUtils.applyColor(config.getString("Settings.prefix"));

        plugin.getServer().getPluginManager().registerEvents(new DSPEvent(), plugin);
        getCommand("칭호").setExecutor(new DSPCommand());
    }

    public void onDisable() {
    }
}

package com.darksoldier1404.dsp;

import com.darksoldier1404.duc.UniversalCore;
import com.darksoldier1404.duc.utils.ConfigUtils;
import com.darksoldier1404.duc.utils.UpdateChecker;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SimplePrefix extends JavaPlugin {
    private UniversalCore core;
    private static SimplePrefix plugin;

    public static SimplePrefix getPlugin() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        Plugin pl = getServer().getPluginManager().getPlugin("DP-UniversalCore");
        if(pl == null) {
            getLogger().warning("DP-UniversalCore 플러그인이 설치되어있지 않습니다.");
            getLogger().warning("DP-SimplePrefix 플러그인을 비활성화 합니다.");
            plugin.setEnabled(false);
            return;
        }
        core = (UniversalCore) pl;
        ConfigUtils.loadDefaultPluginConfig(plugin);
        UpdateChecker.check(plugin);
    }

    public void onDisable() {
    }
}

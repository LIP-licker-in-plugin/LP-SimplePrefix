package com.licker2689.lsp;

import com.licker2689.lpc.utils.ColorUtils;
import com.licker2689.lpc.utils.ConfigUtils;
import com.licker2689.lsp.commands.LSPCommand;
import com.licker2689.lsp.events.LSPEvent;
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
        Plugin pl = getServer().getPluginManager().getPlugin("LP-Core");
        if(pl == null) {
            getLogger().warning("LP-Core 플러그인이 설치되어있지 않습니다.");
            getLogger().warning("LP-SimplePrefix 플러그인을 비활성화 합니다.");
            plugin.setEnabled(false);
            return;
        }
        config = ConfigUtils.loadDefaultPluginConfig(plugin);
        prefix = ColorUtils.applyColor(config.getString("Settings.prefix"));

        plugin.getServer().getPluginManager().registerEvents(new LSPEvent(), plugin);
        getCommand("칭호").setExecutor(new LSPCommand());
    }

    public void onDisable() {
    }
}

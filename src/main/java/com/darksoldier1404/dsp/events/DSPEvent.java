package com.darksoldier1404.dsp.events;

import com.darksoldier1404.dsp.SimplePrefix;
import com.darksoldier1404.duc.utils.ConfigUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.AnvilInventory;

public class DSPEvent implements Listener {
    private final SimplePrefix plugin = SimplePrefix.getInstance();
    private final String prefix = plugin.prefix;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.udata.put(e.getPlayer().getUniqueId(), ConfigUtils.initUserData(plugin, e.getPlayer().getUniqueId().toString(), "/users", new YamlConfiguration()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        YamlConfiguration data = plugin.udata.get(e.getPlayer().getUniqueId());
        ConfigUtils.saveCustomData(plugin, data, "/users", e.getPlayer().getUniqueId().toString());
        plugin.udata.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if(e.getView().getType() == InventoryType.ANVIL) {
            AnvilInventory inv = (AnvilInventory) e.getInventory();
            System.out.println(inv.getRenameText());
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();

    }
}

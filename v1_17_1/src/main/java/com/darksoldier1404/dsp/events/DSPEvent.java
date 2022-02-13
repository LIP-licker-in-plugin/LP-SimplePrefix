package com.darksoldier1404.dsp.events;

import com.darksoldier1404.dsp.SimplePrefix;
import com.darksoldier1404.dsp.functions.DSPFunction;
import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.NBT;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.block.Action.LEFT_CLICK_AIR;
import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;

@SuppressWarnings("all")
public class DSPEvent implements Listener {
    private final SimplePrefix plugin = SimplePrefix.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.udata.put(e.getPlayer().getUniqueId(), ConfigUtils.initUserData(plugin, e.getPlayer().getUniqueId().toString(), "users"));
        ConfigUtils.saveCustomData(plugin, plugin.udata.get(e.getPlayer().getUniqueId()), e.getPlayer().getUniqueId().toString(), "users");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        YamlConfiguration data = plugin.udata.get(e.getPlayer().getUniqueId());
        ConfigUtils.saveCustomData(plugin, data, e.getPlayer().getUniqueId().toString(), "users");
        plugin.udata.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String name = plugin.udata.get(p.getUniqueId()).getString("Player.Prefix") == null ? DSPFunction.giveDefaultPrefix(p) : plugin.udata.get(p.getUniqueId()).getString("Player.Prefix");
        plugin.config.getConfigurationSection("Settings.PrefixList").getKeys(false).forEach(s -> {
            if (s.equals(name)) {
                e.setFormat(ColorUtils.applyColor(plugin.config.getString("Settings.PrefixList." + name)) + e.getFormat());
                return;
            }
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(LEFT_CLICK_AIR) || e.getAction().equals(LEFT_CLICK_BLOCK)) return;
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getItem() == null) return;
        ItemStack item = e.getItem();
        if (!item.hasItemMeta()) return;
        if (NBT.hasTagKey(item, "dsp.prefix")) {
            String name = NBT.getStringTag(item, "dsp.prefix");
            plugin.config.getConfigurationSection("Settings.PrefixList").getKeys(false).forEach(s -> {
                if (s.equals(name)) {
                    if (DSPFunction.givePrefix(e.getPlayer(), name)) {
                        item.setAmount(item.getAmount() - 1);
                        return;
                    }
                }
            });
        }
    }
}

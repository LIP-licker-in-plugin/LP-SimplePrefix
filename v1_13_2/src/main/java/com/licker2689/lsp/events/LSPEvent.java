package com.licker2689.lsp.events;

import com.licker2689.lpc.api.inventory.LInventory;
import com.licker2689.lpc.utils.ColorUtils;
import com.licker2689.lpc.utils.ConfigUtils;
import com.licker2689.lpc.utils.NBT;
import com.licker2689.lsp.SimplePrefix;
import com.licker2689.lsp.functions.LSPFunction;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.event.block.Action.LEFT_CLICK_AIR;
import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;

@SuppressWarnings("all")
public class LSPEvent implements Listener {
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
        if (LSPFunction.currentEditPrefix.containsKey(p.getUniqueId())) {
            e.setCancelled(true);
            String name = LSPFunction.currentEditPrefix.get(p.getUniqueId());
            String prefix = e.getMessage();
            plugin.config.set("Settings.PrefixList." + name, prefix);
            ConfigUtils.savePluginConfig(plugin, plugin.config);
            p.sendMessage(prefix + name + "칭호가 설정되었습니다. : " + ColorUtils.applyColor(prefix));
            LSPFunction.currentEditPrefix.remove(p.getUniqueId());
            return;
        }
        String name = plugin.udata.get(p.getUniqueId()).getString("Player.Prefix") == null ? LSPFunction.giveDefaultPrefix(p) : plugin.udata.get(p.getUniqueId()).getString("Player.Prefix");
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
        if (NBT.hasTagKey(item, "lsp.prefix")) {
            String name = NBT.getStringTag(item, "lsp.prefix");
            plugin.config.getConfigurationSection("Settings.PrefixList").getKeys(false).forEach(s -> {
                if (s.equals(name)) {
                    if (LSPFunction.givePrefix(e.getPlayer(), name)) {
                        item.setAmount(item.getAmount() - 1);
                        return;
                    }
                }
            });
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory() instanceof LInventory) {
            LInventory inv = (LInventory) e.getInventory();
            if (inv.isValidHandler(plugin)) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null) {
                    if(NBT.hasTagKey(e.getCurrentItem(), "next")) {
                        inv.nextPage();
                        LSPFunction.updateCurrentPage(inv);
                        return;
                    }
                    if(NBT.hasTagKey(e.getCurrentItem(), "prev")) {
                        inv.prevPage();
                        LSPFunction.updateCurrentPage(inv);
                        return;
                    }
                    if (NBT.hasTagKey(e.getCurrentItem(), "lsp.prefix")) {
                        String name = NBT.getStringTag(e.getCurrentItem(), "lsp.prefix");
                        LSPFunction.equipPrefix((Player) e.getWhoClicked(), name);
                    }
                }
            }
        }
    }
}

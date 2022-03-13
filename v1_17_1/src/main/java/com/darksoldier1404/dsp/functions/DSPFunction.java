package com.darksoldier1404.dsp.functions;

import com.darksoldier1404.dsp.SimplePrefix;
import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.NBT;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*
 this plugin is using net.wesjd.anvilgui.AnvilGUI API
 XD
 */
@SuppressWarnings("all")
public class DSPFunction {
    private static final SimplePrefix plugin = SimplePrefix.getInstance();
    public static final String prefix = plugin.prefix;

    public static void createPrefix(Player p, String name) {
        plugin.config.set("Settings.PrefixList." + name, "설정 필요");
        ConfigUtils.savePluginConfig(plugin, plugin.config);
        p.sendMessage(prefix + name + " 칭호가 생성되었습니다.");
    }

    public static void deletePrefix(Player p, String name) {
        plugin.config.set("Settings.PrefixList." + name, null);
        ConfigUtils.savePluginConfig(plugin, plugin.config);
        p.sendMessage(prefix + name + " 칭호가 삭제되었습니다.");
    }

    public static void openSetPrefixGUI(Player p, String name) {
        ItemStack item = new ItemStack(Material.valueOf(plugin.config.getString("Settings.couponMaterial")));
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        item.setItemMeta(im);
        new AnvilGUI.Builder()
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    plugin.config.set("Settings.PrefixList." + name, text);
                    ConfigUtils.savePluginConfig(plugin, plugin.config);
                    player.sendMessage(prefix + name + "칭호가 설정되었습니다. : " + ColorUtils.applyColor(text));
                    return AnvilGUI.Response.close();
                })
                .preventClose()                                                    //prevents the inventory from being closed
                .text("보여지게될 칭호를 설정하세요.")                              //sets the text the GUI should start with
                .itemLeft(item)                      //use a custom item for the first slot
                .itemRight(null)                     //use a custom item for the second slot
//                .onLeftInputClick(player -> player.sendMessage("first sword"))     //called when the left input slot is clicked
//                .onRightInputClick(player -> player.sendMessage("second sword"))   //called when the right input slot is clicked
                .title(name + " 칭호 설정")                                       //set the title of the GUI (only works in 1.14+)
                .plugin(plugin)                                          //set the plugin instance
                .open(p);                                                   //opens the GUI for the player provided
    }

    public static void showAllPrefixList(Player p) {
        p.sendMessage(prefix + "<<< 모든 칭호 목록 >>>");
        for (String key : plugin.config.getConfigurationSection("Settings.PrefixList").getKeys(false)) {
            p.sendMessage(prefix + key + " : " + ColorUtils.applyColor(plugin.config.getString("Settings.PrefixList." + key)));
        }
    }

    public static void showPrefixList(Player p) {
        if (plugin.udata.get(p.getUniqueId()).getList("Player.PrefixList") == null) {
            p.sendMessage(prefix + "칭호가 없습니다.");
            return;
        }
        p.sendMessage(prefix + "<<< 보유 칭호 목록 >>>");
        List<String> list = (List<String>) plugin.udata.get(p.getUniqueId()).getList("Player.PrefixList");
        for (String key : list) {
            String s = plugin.config.getString("Settings.PrefixList." + key);
            if(s != null) {
                p.sendMessage(prefix + key + " : " + ColorUtils.applyColor(s));
            }
        }
    }

    public static void equipPrefix(Player p, String name) {
        YamlConfiguration data = plugin.udata.get(p.getUniqueId());
        if(data.getList("Player.PrefixList") != null) {
            try {
                List<String> list = (List<String>) data.getList("Player.PrefixList");
                if (list.contains(name)) {
                    if (data.getString("Player.Prefix") != null && data.getString("Player.Prefix").equals(name)) {
                        p.sendMessage(prefix + "이미 장착중인 칭호입니다.");
                        return;
                    }
                    data.set("Player.Prefix", name);
                    p.sendMessage(prefix + name + " 칭호가 장착되었습니다.");
                    ConfigUtils.saveCustomData(plugin, plugin.udata.get(p.getUniqueId()), p.getUniqueId().toString(), "users");
                } else {
                    p.sendMessage(prefix + "보유중인 칭호가 아닙니다.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("칭호 장착 오류");
                p.sendMessage(prefix + "보유중인 칭호가 아닙니다.");
            }
        }else{
            p.sendMessage(prefix + "보유중인 칭호가 아닙니다.");
        }
    }

    public static void unequipPrefix(Player p) {
        YamlConfiguration data = plugin.udata.get(p.getUniqueId());
        if (data.getString("Player.Prefix") == null) {
            p.sendMessage(prefix + "장착중인 칭호가 없습니다.");
            return;
        }
        data.set("Player.Prefix", "");
        p.sendMessage(prefix + "칭호가 해제되었습니다.");
        ConfigUtils.saveCustomData(plugin, plugin.udata.get(p.getUniqueId()), p.getUniqueId().toString(), "users");
    }


    public static boolean givePrefix(Player p, String name) {
        YamlConfiguration data = plugin.udata.get(p.getUniqueId());
        if (data == null) {
            System.out.println("data is null");
            return false;
        }
        if (data.get("Player.PrefixList") != null && data.getList("Player.PrefixList").contains(name)) {
            p.sendMessage(prefix + "이미 보유중인 칭호입니다.");
            return false;
        }
        List<String> list;
        if(data.getStringList("Player.PrefixList") == null || data.getStringList("Player.PrefixList").isEmpty()) {
            list = new ArrayList<>();
        }else{
            list = (List<String>) data.getList("Player.PrefixList");
        }
        list.add(name);
        data.set("Player.PrefixList", list);
        p.sendMessage(prefix + name + " 칭호를 획득하였습니다.");
        ConfigUtils.saveCustomData(plugin, plugin.udata.get(p.getUniqueId()), p.getUniqueId().toString(), "users");
        return true;
    }

    public static void getPrefixCoupon(Player p, String name) {
        if (plugin.config.getConfigurationSection("Settings.PrefixList").getKeys(false).contains(name)) {
            String prefix = plugin.config.getString("Settings.PrefixList." + name);
            ItemStack item = new ItemStack(Material.valueOf(plugin.config.getString("Settings.couponMaterial")));
            ItemMeta im = item.getItemMeta();
            // set display name with placeholder
            im.setDisplayName(ColorUtils.applyColor(plugin.config.getString("Settings.couponCustomName").replace("%dsp.prefix%", prefix)));
            List<String> lore = plugin.config.getStringList("Settings.couponLores");
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, ColorUtils.applyColor(lore.get(i).replace("%dsp.prefix%", prefix)));
            }
            im.setLore(lore);
            item.setItemMeta(im);
            item = NBT.setStringTag(item, "dsp.prefix", name);
            p.getInventory().addItem(item);
            p.sendMessage(ColorUtils.applyColor(prefix) + "칭호 쿠폰을 발급하였습니다.");
        }
    }

    public static void setDefaultPrefix(Player p, String name) {
        plugin.config.set("Settings.DefaultPrefix", name);
        ConfigUtils.savePluginConfig(plugin, plugin.config);
        p.sendMessage(prefix + name + " 칭호가 기본 칭호로 설정되었습니다.");
        plugin.udata.values().forEach(data -> {
            if (!(data.get("Player.PrefixList") != null && data.getList("Player.PrefixList").contains(name))) {
                List<String> list;
                if(data.getStringList("Player.PrefixList") == null || data.getStringList("Player.PrefixList").isEmpty()) {
                    list = new ArrayList<>();
                }else{
                    list = (List<String>) data.getList("Player.PrefixList");
                }
                list.add(name);
                data.set("Player.PrefixList", list);
                if(data.getString("Player.Prefix") == null) {
                    data.set("Player.Prefix", name);
                }
            }
        });
    }

    public static String giveDefaultPrefix(Player p) {
        if (plugin.config.getString("Settings.DefaultPrefix") == null) {
            return "";
        }
        YamlConfiguration data = plugin.udata.get(p.getUniqueId());
        String name = plugin.config.getString("Settings.DefaultPrefix");
        if (!(data.get("Player.PrefixList") != null && data.getList("Player.PrefixList").contains(name))) {
            List<String> list;
            if(data.getStringList("Player.PrefixList") == null || data.getStringList("Player.PrefixList").isEmpty()) {
                list = new ArrayList<>();
            }else{
                list = (List<String>) data.getList("Player.PrefixList");
            }
            list.add(name);
            data.set("Player.PrefixList", list);
            if(data.getString("Player.Prefix") == null) {
                data.set("Player.Prefix", name);
            }
        }
        return plugin.config.getString("Settings.PrefixList." + name);
    }
}

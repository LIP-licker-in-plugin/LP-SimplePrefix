package com.darksoldier1404.dsp.functions;

import com.darksoldier1404.dsp.SimplePrefix;
import com.darksoldier1404.duc.utils.ConfigUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 this plugin is using the API of AnvilGUI
 XD
 */

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
                    player.sendMessage(prefix + name + "칭호가 설정되었습니다. : " + ChatColor.translateAlternateColorCodes('&', text));
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
}

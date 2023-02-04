package com.darksoldier1404.dsp.functions;

import com.darksoldier1404.dppc.api.inventory.DInventory;
import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.ConfigUtils;
import com.darksoldier1404.dppc.utils.NBT;
import com.darksoldier1404.dsp.SimplePrefix;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@SuppressWarnings("all")
public class DSPFunction {
    private static final SimplePrefix plugin = SimplePrefix.getInstance();
    public static final String prefix = plugin.prefix;
    public static final Map<UUID, String> currentEditPrefix = new HashMap<>();

    public static void updateCurrentPage(DInventory inv) {
        ItemStack[] tools = inv.getPageTools();
        ItemStack item = tools[4];
        ItemMeta im = item.getItemMeta();
        im.setDisplayName("§a현재 페이지: §f" + (inv.getCurrentPage() + 1));
        item.setItemMeta(im);
        tools[4] = item;
        inv.setPageTools(tools);
        inv.update();
    }

    public static void createPrefix(Player p, String name) {
        if(plugin.config.get("Prefix.PrefixList." + name) != null) {
            p.sendMessage(prefix + "§c이미 존재하는 칭호 이름입니다.");
            return;
        }
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
        currentEditPrefix.put(p.getUniqueId(), name);
        p.sendMessage(prefix + "설정할 칭호를 채팅으로 입력해주시면 됩니다.");
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
        List<String> list = (List<String>) plugin.udata.get(p.getUniqueId()).getList("Player.PrefixList");
        List<ItemStack> prefixs = new ArrayList<>();
        for (String key : list) {
            String s = plugin.config.getString("Settings.PrefixList." + key);
            if (s != null) {
                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta im = item.getItemMeta();
                im.setDisplayName(ColorUtils.applyColor(s));
                im.setLore(Arrays.asList("", "§6클릭하여 해당 칭호로 변경하세요!", "§7Raw Name : " + key));
                item.setItemMeta(im);
                prefixs.add(NBT.setStringTag(item, "dsp.prefix", key));
            }
        }
        DInventory inv = new DInventory(null, "§6보유 칭호 목록", 54, true, plugin);
        ItemStack pane = NBT.setStringTag(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), "page", "true");
        ItemStack prev = NBT.setStringTag(new ItemStack(Material.PINK_DYE), "prev", "true");
        ItemStack current = NBT.setStringTag(new ItemStack(Material.PAPER), "current", "true");
        ItemMeta im = prev.getItemMeta();
        im.setDisplayName("이전 페이지");
        prev.setItemMeta(im);
        ItemStack next = NBT.setStringTag(new ItemStack(Material.LIME_DYE), "next", "true");
        im = next.getItemMeta();
        im.setDisplayName("다음 페이지");
        next.setItemMeta(im);
        im = current.getItemMeta();
        im.setDisplayName("§a현재 페이지: §f" + (inv.getCurrentPage() + 1));
        current.setItemMeta(im);
        inv.setPageTools(new ItemStack[]{pane, pane, prev, pane, current, pane, next, pane, pane});
        inv.setPages(0);

        int count = 0;
        // get int maxPages it should be start with 0 and if prefixs.size() is can divide 45, maxPages is prefixs.size() / 45 - 1
        int maxPages = prefixs.size() % 45 == 0 ? prefixs.size() / 45 - 1 : prefixs.size() / 45;
        ItemStack[] contents = new ItemStack[45];
        inv.setPages(maxPages);
        int page = 0;
        for (ItemStack item : prefixs) {
            if (count == 45) {
                inv.setPageContent(page, contents);
                contents = new ItemStack[45];
                count = 0;
                page++;
            }
            contents[count] = item;
            count++;
        }
        if(count != 0) {
            inv.setPageContent(page, contents);
        }
        inv.update();
        p.openInventory(inv);
    }

    public static void equipPrefix(Player p, String name) {
        YamlConfiguration data = plugin.udata.get(p.getUniqueId());
        if (data.getList("Player.PrefixList") != null) {
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
        } else {
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
        if (data.getStringList("Player.PrefixList") == null || data.getStringList("Player.PrefixList").isEmpty()) {
            list = new ArrayList<>();
        } else {
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
            p.getInventory().addItem(NBT.setStringTag(item, "dsp.prefix", name));
            p.sendMessage(ColorUtils.applyColor(prefix) + "칭호 쿠폰을 발급하였습니다.");
        } else {
            p.sendMessage(prefix + "존재하지 않는 칭호입니다.");
        }
    }

    public static void setDefaultPrefix(Player p, String name) {
        plugin.config.set("Settings.DefaultPrefix", name);
        ConfigUtils.savePluginConfig(plugin, plugin.config);
        p.sendMessage(prefix + name + " 칭호가 기본 칭호로 설정되었습니다.");
        plugin.udata.values().forEach(data -> {
            if (!(data.get("Player.PrefixList") != null && data.getList("Player.PrefixList").contains(name))) {
                List<String> list;
                if (data.getStringList("Player.PrefixList") == null || data.getStringList("Player.PrefixList").isEmpty()) {
                    list = new ArrayList<>();
                } else {
                    list = (List<String>) data.getList("Player.PrefixList");
                }
                list.add(name);
                data.set("Player.PrefixList", list);
                if (data.getString("Player.Prefix") == null) {
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
            if (data.getStringList("Player.PrefixList") == null || data.getStringList("Player.PrefixList").isEmpty()) {
                list = new ArrayList<>();
            } else {
                list = (List<String>) data.getList("Player.PrefixList");
            }
            list.add(name);
            data.set("Player.PrefixList", list);
            if (data.getString("Player.Prefix") == null) {
                data.set("Player.Prefix", name);
            }
        }
        return plugin.config.getString("Settings.PrefixList." + name);
    }
}

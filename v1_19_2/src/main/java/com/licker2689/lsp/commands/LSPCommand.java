package com.licker2689.lsp.commands;

import com.licker2689.lsp.SimplePrefix;
import com.licker2689.lsp.functions.LSPFunction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class LSPCommand implements CommandExecutor, TabCompleter {
    private final String prefix = SimplePrefix.getInstance().prefix;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "§c플레이어만 사용 가능한 명령어 입니다.");
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.isOp()) {
                sender.sendMessage(prefix + "/칭호 생성 <칭호> - 칭호를 생성합니다.");
                sender.sendMessage(prefix + "/칭호 설정 <칭호> - 화면에 보이게될 칭호를 설정합니다.");
                sender.sendMessage(prefix + "/칭호 삭제 <칭호> - 칭호를 삭제합니다.");
                sender.sendMessage(prefix + "/칭호 쿠폰 <칭호> (닉네임) - 해당 칭호의 쿠폰 아이템을 받습니다. 우클릭시 칭호를 획득하며 중복 획득은 불가능합니다.");
                sender.sendMessage(prefix + "/칭호 기본 <칭호> - 해당 칭호를 기본 칭호로 설정하고 접속하는 모든 유저에게 해당 칭호를 지급합니다.");
                sender.sendMessage(prefix + "/칭호 모든목록 - 모든 칭호 목록을 확인합니다.");
            }
            sender.sendMessage(prefix + "/칭호 장착 <칭호> - 칭호를 장착합니다.");
            sender.sendMessage(prefix + "/칭호 장착해제 - 칭호를 장착해제 합니다.");
            sender.sendMessage(prefix + "/칭호 목록 - 보유중인 칭호 목록을 확인합니다.");
            return false;
        }
        if (args[0].equals("장착")) {
            if (args.length == 1) {
                sender.sendMessage(prefix + "/칭호 장착 <칭호> - 칭호를 장착합니다.");
                return false;
            }
            if (args.length == 2) {
                LSPFunction.equipPrefix(p, args[1]);
                return false;
            }
            sender.sendMessage(prefix + "/칭호 장착 <칭호> - 칭호를 장착합니다.");
            return false;
        }
        if (args[0].equals("장착해제")) {
            if (args.length == 1) {
                LSPFunction.unequipPrefix(p);
                return false;
            }
            sender.sendMessage(prefix + "/칭호 장착해제 - 칭호를 장착해제 합니다.");
            return false;
        }
        if (args[0].equals("목록")) {
            if (args.length == 1) {
                LSPFunction.showPrefixList(p);
                return false;
            }
            return false;
        }
        if (p.isOp()) {
            if (args[0].equals("생성")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 생성 <칭호>");
                    return false;
                }
                LSPFunction.createPrefix(p, args[1]);
                return false;
            }
            if (args[0].equals("설정")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 설정 <칭호>");
                    return false;
                }
                LSPFunction.openSetPrefixGUI(p, args[1]);
                return false;
            }
            if (args[0].equals("삭제")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 삭제 <칭호>");
                    return false;
                }
                LSPFunction.deletePrefix(p, args[1]);
                return false;
            }
            if (args[0].equals("쿠폰")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 쿠폰 <칭호> (닉네임)");
                    return false;
                }
                if(args.length == 2) {
                    LSPFunction.getPrefixCoupon(p, args[1]);
                    return false;
                }
                if(args.length == 3) {
                    try{
                        LSPFunction.getPrefixCoupon(Bukkit.getPlayer(args[2]), args[1]);
                    }catch (Exception e) {
                        p.sendMessage(prefix + "해당 플레이어는 존재하지 않습니다.");
                        return false;
                    }
                    return false;
                }
                return false;
            }
            if(args[0].equals("기본")) {
                if(args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 기본 <칭호>");
                    return false;
                }
                LSPFunction.setDefaultPrefix(p, args[1]);
                return false;
            }
            if (args[0].equals("모든목록")) {
                LSPFunction.showAllPrefixList(p);
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender.isOp()) {
                return Arrays.asList("생성", "설정", "삭제", "쿠폰", "모든목록", "장착", "장착해제", "목록", "기본");
            } else {
                return Arrays.asList("장착", "장착해제", "목록");
            }
        }
        return null;
    }
}

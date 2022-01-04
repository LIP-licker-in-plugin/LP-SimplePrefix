package com.darksoldier1404.dsp.commands;

import com.darksoldier1404.dsp.SimplePrefix;
import com.darksoldier1404.dsp.functions.DSPFunction;
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
public class DSPCommand implements CommandExecutor, TabCompleter {
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
                sender.sendMessage(prefix + "/칭호 쿠폰 <칭호> - 해당 칭호의 쿠폰 아이템을 받습니다. 우클릭시 칭호를 획득하며 중복 획득은 불가능합니다.");
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
                DSPFunction.equipPrefix(p, args[1]);
                return false;
            }
            return false;
        }
        if (args[0].equals("장착해제")) {
            if (args.length == 1) {
                DSPFunction.unequipPrefix(p);
                return false;
            }
        }
        if (args[0].equals("목록")) {
            if (args.length == 1) {
                DSPFunction.showPrefixList(p);
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
                DSPFunction.createPrefix(p, args[1]);
                return false;
            }
            if (args[0].equals("설정")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 설정 <칭호>");
                    return false;
                }
                DSPFunction.openSetPrefixGUI(p, args[1]);
                return false;
            }
            if (args[0].equals("삭제")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 삭제 <칭호>");
                    return false;
                }
                DSPFunction.deletePrefix(p, args[1]);
                return false;
            }
            if (args[0].equals("쿠폰")) {
                if (args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 쿠폰 <칭호>");
                    return false;
                }
                DSPFunction.getPrefixCoupon(p, args[1]);
                return false;
            }
            if(args[0].equals("기본")) {
                if(args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 기본 <칭호>");
                    return false;
                }
                DSPFunction.setDefaultPrefix(p, args[1]);
                return false;
            }
            if (args[0].equals("모든목록")) {
                DSPFunction.showAllPrefixList(p);
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

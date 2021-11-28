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

import java.util.List;

public class DSPCommand implements CommandExecutor, TabCompleter {
    private final String prefix = SimplePrefix.getInstance().prefix;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage(prefix + "§c플레이어만 사용 가능한 명령어 입니다.");
            return false;
        }
        if(args.length == 0) {
            if(p.isOp()) {
                sender.sendMessage(prefix + "/칭호 생성 <칭호> - 칭호를 생성합니다.");
                sender.sendMessage(prefix + "/칭호 설정 <칭호> - 화면에 보이게될 칭호를 설정합니다.");
                sender.sendMessage(prefix + "/칭호 삭제 <칭호> - 칭호를 삭제합니다.");
                sender.sendMessage(prefix + "/칭호 쿠폰 <칭호> - 해당 칭호의 쿠폰 아이템을 받습니다. 우클릭시 칭호를 획득하며 중복 획득은 불가능합니다.");
            }
            sender.sendMessage(prefix + "/칭호 장착 <칭호> - 칭호를 장착합니다.");
            sender.sendMessage(prefix + "/칭호 목록 - 보유중인 칭호 목록을 확인합니다.");
            return false;
        }
        if(args[0].equals("장착")) {
            if(args.length == 1) {
                sender.sendMessage(prefix + "/칭호 장착 <칭호> - 칭호를 장착합니다.");
                return false;
            }
            return false;
        }
        if(args[0].equals("목록")) {
            if(args.length == 1) {

            }
            return false;
        }
        if(p.isOp()) {
            if(args[0].equals("생성")) {
                if(args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 생성 <칭호>");
                    return false;
                }
                DSPFunction.createPrefix(p, args[1]);
                return false;
            }
            if(args[0].equals("설정")) {
                if(args.length == 1) {
                    sender.sendMessage(prefix + "§c/칭호 설정 <칭호>");
                    return false;
                }
                DSPFunction.openSetPrefixGUI(p, args[1]);
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

package cn.chengzhiya.mhdftools.command;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.interfaces.Command;
import cn.chengzhiya.mhdftools.util.action.ActionUtil;
import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
public abstract class AbstractCommand implements TabExecutor, Command {
    private final boolean enable;
    private final String description;
    private final String permission;
    private final boolean onlyPlayer;
    private final String[] commands;

    public AbstractCommand(List<String> enableKeyList, @NotNull String description, String permission, boolean onlyPlayer, String... commands) {
        boolean enable = true;
        for (String enableKey : enableKeyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.enable = enable;
        this.description = description;
        this.permission = permission;
        this.onlyPlayer = onlyPlayer;
        this.commands = commands;
    }

    public AbstractCommand(String enableKey, @NotNull String description, String permission, boolean onlyPlayer, String... commands) {
        this(List.of(enableKey), description, permission, onlyPlayer, commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (onlyPlayer) {
            if (sender instanceof Player player) {
                execute(player, label, args);
            } else {
                ActionUtil.sendMessage(sender, LangUtil.i18n("onlyPlayer"));
            }
            return false;
        }
        execute(sender, label, args);
        return false;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabComplete = tabCompleter(sender, label, args);
        if (tabComplete == null) {
            tabComplete = Main.instance.getBungeeCordManager().getBukkitPlayerList();
        }
        return tabComplete.stream()
                .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT)))
                .toList();
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public void execute(@NotNull Player sender, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public List<String> tabCompleter(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return new ArrayList<>();
    }
}

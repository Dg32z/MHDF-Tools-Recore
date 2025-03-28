package cn.chengzhiya.mhdftools.util.action;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.feature.CustomMenuUtil;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import cn.chengzhiya.mhdftools.util.scheduler.MHDFScheduler;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

@SuppressWarnings({"unused", "deprecation"})
public final class ActionUtil {
    /**
     * 发送全服消息
     *
     * @param message 文本实例
     */
    public static void sendMessage(CommandSender sender, TextComponent message) {
        if (sender == null) {
            return;
        }

        Main.adventure.sender(sender).sendMessage(message);
    }

    /**
     * 给指定命令执行者实例发送消息
     *
     * @param sender  命令执行者实例
     * @param message 消息
     */
    public static void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, ColorUtil.color(message));
    }

    /**
     * 发送全服消息
     *
     * @param message 文本实例
     */
    public static void broadcastMessage(TextComponent message) {
        Bukkit.broadcast(message);
    }

    /**
     * 发送全服消息
     *
     * @param message 消息
     */
    public static void broadcastMessage(String message) {
        broadcastMessage(ColorUtil.color(message));
    }

    /**
     * 给指定玩家实例播放音效
     *
     * @param player 玩家实例
     * @param sound  音效
     * @param volume 音量
     * @param pitch  音调
     */
    public static void playSound(Player player, Sound sound, Float volume, Float pitch) {
        MHDFScheduler.getAsyncScheduler().runNow(Main.instance, task ->
                player.playSound(player, sound, volume, pitch));
    }

    /**
     * 给指定玩家实例播放音效
     *
     * @param player 玩家实例
     * @param sound  音效
     * @param volume 音量
     * @param pitch  音调
     */
    public static void playSound(Player player, String sound, Float volume, Float pitch) {
        MHDFScheduler.getAsyncScheduler().runNow(Main.instance, task ->
                player.playSound(player, sound, volume, pitch));
    }

    /**
     * 给指定玩家实例发送标题消息
     *
     * @param player   玩家实例
     * @param title    大标题文本
     * @param subTitle 小标题文本吗
     * @param fadeIn   淡入时间
     * @param stay     停留时间
     * @param fadeOut  淡出时间
     */
    public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        MHDFScheduler.getAsyncScheduler().runNow(Main.instance, task -> {
            Main.adventure.player(player).sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofMillis(fadeIn * 50L), Duration.ofMillis(stay * 50L), Duration.ofMillis(fadeOut * 50L)));
            Main.adventure.player(player).sendTitlePart(TitlePart.SUBTITLE, ColorUtil.color(subTitle));
            Main.adventure.player(player).sendTitlePart(TitlePart.TITLE, ColorUtil.color(title));
        });
    }

    /**
     * 给指定玩家实例发送标题消息
     *
     * @param player   玩家实例
     * @param title    大标题文本
     * @param subTitle 小标题文本
     */
    public static void sendTitle(Player player, String title, String subTitle) {
        sendTitle(player, title, subTitle, 10, 70, 20);
    }

    /**
     * 给指定玩家实例发送标题消息
     *
     * @param player 玩家实例
     * @param title  大标题文本
     */
    public static void sendTitle(Player player, String title) {
        sendTitle(player, title, "", 10, 70, 20);
    }

    /**
     * 给指定玩家实例发送操作栏消息
     *
     * @param player  玩家实例
     * @param message 消息
     */
    public static void sendActionBar(Player player, String message) {
        MHDFScheduler.getAsyncScheduler().runNow(Main.instance, task ->
                Main.adventure.player(player).sendActionBar(ColorUtil.color(message)));
    }

    /**
     * 给指定玩家实例发送BOSS血条
     *
     * @param player  玩家实例
     * @param bossBar BOSS血条实例
     */
    public static void sendBossbar(Player player, BossBar bossBar) {
        MHDFScheduler.getAsyncScheduler().runNow(Main.instance, task ->
                Main.adventure.player(player).showBossBar(bossBar));
    }

    /**
     * 隐藏指定玩家实例的指定BOSS血条
     *
     * @param player  玩家实例
     * @param bossBar BOSS血条实例
     */
    public static void hideBossbar(Player player, BossBar bossBar) {
        MHDFScheduler.getAsyncScheduler().runNow(Main.instance, task ->
                Main.adventure.player(player).hideBossBar(bossBar));
    }

    /**
     * 给指定玩家实例发送BOSS血条
     *
     * @param player  玩家实例
     * @param bossBar BOSS血条实例
     * @param time    停留时间
     */
    public static void sendTimeBossbar(Player player, BossBar bossBar, Long time) {
        sendBossbar(player, bossBar);
        MHDFScheduler.getAsyncScheduler().runAtFixedRate(Main.instance, task ->
                hideBossbar(player, bossBar), 0, time);
    }

    /**
     * 执行命令
     *
     * @param sender  命令执行者实例
     * @param command 命令
     */
    public static void runCommand(CommandSender sender, String command) {
        MHDFScheduler.getGlobalRegionScheduler().run(Main.instance, task ->
                Bukkit.dispatchCommand(sender, command));
    }

    /**
     * 以op身份执行命令
     *
     * @param sender  命令执行者实例
     * @param command 命令
     */
    public static void runOpCommand(CommandSender sender, String command) {
        if (sender instanceof Player player) {
            player.setOp(true);
            runCommand(player, command);
            player.setOp(false);
            return;
        }
        runCommand(Bukkit.getConsoleSender(), command);
    }

    /**
     * 执行操作
     *
     * @param sender 命令执行者实例
     * @param args   操作参数
     */
    public static void runAction(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "[connect]" -> {
                if (sender instanceof Player player) {
                    Main.instance.getBungeeCordManager().connectServer(player.getName(), args[1]);
                }
            }
            case "[player]" -> runCommand(sender, args[1]);
            case "[player_op]" -> runOpCommand(sender, args[1]);
            case "[console]" -> runCommand(Bukkit.getConsoleSender(), args[1]);
            case "[broadcast]" -> Main.instance.getBungeeCordManager().broadcastMessage(args[1]);
            case "[message]" -> sendMessage(sender, args[1]);
            case "[actionbar]" -> {
                if (sender instanceof Player player) {
                    sendActionBar(player, args[1]);
                }
            }
            case "[bossbar]" -> {
                if (sender instanceof Player player) {
                    sendBossbar(
                            player,
                            BossBarUtil.getBossBar(
                                    args[1],
                                    BossBar.Color.valueOf(args[2]),
                                    BossBar.Overlay.valueOf(args[3])
                            )
                    );
                }
            }
            case "[title]" -> {
                if (sender instanceof Player player) {
                    sendTitle(
                            player,
                            args[1],
                            args[2],
                            Integer.parseInt(args[3]),
                            Integer.parseInt(args[4]),
                            Integer.parseInt(args[5])
                    );
                }
            }
            case "[chat]" -> {
                if (sender instanceof Player player) {
                    player.chat(args[1]);
                }
            }
            case "[sound_bukkit]" -> {
                if (sender instanceof Player player) {
                    playSound(
                            player,
                            SoundUtil.getSound(args[1]),
                            Float.valueOf(args[2]),
                            Float.valueOf(args[3])
                    );
                }
            }
            case "[sound_minecraft]" -> {
                if (sender instanceof Player player) {
                    playSound(
                            player,
                            args[1],
                            Float.valueOf(args[2]),
                            Float.valueOf(args[3])
                    );
                }
            }
            case "[menu]" -> {
                if (sender instanceof Player player) {
                    CustomMenuUtil.openCustomMenu(player, args[1]);
                }
            }
            case "[close]" -> {
                if (sender instanceof Player player) {
                    player.closeInventory();
                }
            }
        }
    }

    /**
     * 执行操作列表
     *
     * @param sender     命令执行者实例
     * @param actionList 操作列表
     */
    public static void runActionList(CommandSender sender, List<String> actionList) {
        for (String action : actionList) {
            if (sender instanceof Player player) {
                action = Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(player, action);
            } else {
                action = Main.instance.getPluginHookManager().getPlaceholderAPIHook().placeholder(null, action);
            }

            String[] args = action.split("<delay=");
            long delay = args.length > 1 ? Long.parseLong(args[1].replace(">", "")) : 0;

            MHDFScheduler.getAsyncScheduler().runDelayed(Main.instance, (task) -> runAction(sender, args[0].split("\\|")), delay);
        }
    }
}
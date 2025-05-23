package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.Main;
import cn.chengzhiya.mhdftools.command.AbstractCommand;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.util.PluginUtil;
import cn.chengzhiya.mhdftools.util.config.LangUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import cn.chengzhiya.mhdftools.util.reflection.ReflectionUtil;
import lombok.Getter;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@SuppressWarnings({"deprecation", "unused"})
public final class CommandManager {
    private final List<String> registerCommandIdList = new ArrayList<>();

    /**
     * 注册所有启用的命令
     */
    public void init() {
        try {
            Reflections reflections = new Reflections(AbstractCommand.class.getPackageName());

            for (Class<? extends AbstractCommand> clazz : reflections.getSubTypesOf(AbstractCommand.class)) {
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    AbstractCommand command = clazz.getDeclaredConstructor().newInstance();
                    if (command.isEnable()) {
                        registerCommand(command);
                        getRegisterCommandIdList().add(command.getPermission()
                                .replace("mhdftools.commands.", "")
                        );
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册命令
     *
     * @param abstractCommand 命令实例
     */
    private void registerCommand(AbstractCommand abstractCommand) throws Exception {
        Constructor<PluginCommand> commandConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        commandConstructor.setAccessible(true);
        if (abstractCommand.getCommands().length == 0) {
            LogUtil.log("命令: " + abstractCommand.getClass() + " 注册失败, 原因: 找不到命令!");
            return;
        }

        PluginCommand command = commandConstructor.newInstance(abstractCommand.getCommands()[0], Main.instance);

        command.setAliases(Arrays.asList(abstractCommand.getCommands()));
        command.setDescription(abstractCommand.getDescription());
        command.setPermission(abstractCommand.getPermission());

        TextComponent permissionMessage = LangUtil.i18n("noPermission");
        if (Main.instance.isNativeSupportAdventureApi()) {
            command.permissionMessage(permissionMessage);
        } else {
            command.setPermissionMessage(permissionMessage.toLegacyString());
        }

        command.setExecutor(abstractCommand);
        command.setTabCompleter(abstractCommand);

        CommandMap commandMap;
        try {
            commandMap = Main.instance.getServer().getCommandMap();
        } catch (NoSuchMethodError e) {
            commandMap = ReflectionUtil.getFieldValue(
                    ReflectionUtil.getField(
                            Main.instance.getServer().getClass(),
                            "commandMap",
                            true
                    ),
                    Main.instance.getServer()
            );
        }
        commandMap.register(PluginUtil.getName(), command);
    }
}

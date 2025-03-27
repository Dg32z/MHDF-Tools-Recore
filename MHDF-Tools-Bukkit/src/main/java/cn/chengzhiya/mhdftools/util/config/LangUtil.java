package cn.chengzhiya.mhdftools.util.config;

import cn.chengzhiya.mhdftools.exception.ResourceException;
import cn.chengzhiya.mhdftools.text.TextComponent;
import cn.chengzhiya.mhdftools.text.TextComponentBuilder;
import cn.chengzhiya.mhdftools.util.message.ColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class LangUtil {
    private static final File file = new File(ConfigUtil.getDataFolder(), "lang.yml");
    private static YamlConfiguration data;

    /**
     * 获取语言文件文件名称
     *
     * @return 语言文件文件名称
     */
    public static String getLangFileName() {
        return "lang_zh.yml";
    }

    /**
     * 保存初始语言文件
     */
    public static void saveDefaultLang() throws ResourceException {
        FileUtil.saveResource("lang.yml", getLangFileName(), false);
    }

    /**
     * 加载语言文件
     */
    public static void reloadLang() {
        YamlUtil.updateConfig(file, getLangFileName());
        data = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 根据指定key获取语言文件对应文本
     *
     * @return 文本
     */
    public static @NotNull String getString(String key) {
        if (data == null) {
            reloadLang();
        }
        String value = data.getString(key);
        return value != null ? value : "";
    }

    /**
     * 根据指定key获取语言文件对应文本并处理颜色
     *
     * @return 文本
     */
    public static @NotNull TextComponent i18n(String key) {
        return ColorUtil.color(getString(key));
    }

    /**
     * 获取指定key下的项列表
     *
     * @return 项列表
     */
    public static @NotNull Set<String> getKeys(String key) {
        if (data == null) {
            reloadLang();
        }
        return Objects.requireNonNull(data.getConfigurationSection(key)).getKeys(false);
    }

    /**
     * 获取所有命令帮助
     *
     * @return 命令帮助文本
     */
    public static @NotNull TextComponent getHelpList(String commandKey) {
        TextComponentBuilder textComponentBuilder = new TextComponentBuilder();

        List<String> keys = new ArrayList<>(LangUtil.getKeys("commands." + commandKey + ".subCommands"));
        for (String key : keys) {
            textComponentBuilder.append(
                    LangUtil.i18n("commands." + commandKey + ".subCommands.help.commandFormat")
                            .replace("{usage}",
                                    LangUtil.i18n("commands." + commandKey + ".subCommands." + key + ".usage")
                            )
                            .replace("{description}",
                                    LangUtil.i18n("commands." + commandKey + ".subCommands." + key + ".description")
                            )
            );
            if (!key.equals(keys.get(keys.size() - 1))) {
                textComponentBuilder.appendNewline();
            }
        }

        return textComponentBuilder.build();
    }
}

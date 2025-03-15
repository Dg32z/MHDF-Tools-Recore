package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.manager.*;
import cn.chengzhiya.mhdftools.util.config.MinecraftLangUtil;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    public static Main instance;
    public static BukkitAudiences adventure;
    private ConfigManager configManager;
    private LibrariesManager librariesManager;
    private DatabaseManager databaseManager;
    private CacheManager cacheManager;
    private BStatsManager bStatsManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private TaskManager taskManager;
    private BungeeCordManager bungeeCordManager;
    private PluginHookManager pluginHookManager;

    @Override
    public void onLoad() {
        instance = this;

        this.librariesManager = new LibrariesManager();
        this.librariesManager.init();

        this.configManager = new ConfigManager();
        this.configManager.init();
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        this.databaseManager = new DatabaseManager();
        this.databaseManager.init();

        this.cacheManager = new CacheManager();
        this.cacheManager.init();

        this.bStatsManager = new BStatsManager();
        this.bStatsManager.init();

        this.pluginHookManager = new PluginHookManager();
        this.pluginHookManager.hook();

        MinecraftLangUtil.saveDefaultMinecraftLang();
        MinecraftLangUtil.reloadMinecraftLang();

        this.commandManager = new CommandManager();
        this.commandManager.init();

        this.listenerManager = new ListenerManager();
        this.listenerManager.init();

        this.taskManager = new TaskManager();
        this.taskManager.init();

        this.bungeeCordManager = new BungeeCordManager();
        this.bungeeCordManager.init();

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件启动成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }

    @Override
    public void onDisable() {
        if (adventure != null) {
            adventure.close();
        }

        if (this.pluginHookManager != null) {
            this.pluginHookManager.unhook();
        }
        if (this.bungeeCordManager != null) {
            this.bungeeCordManager.close();
        }
        if (this.cacheManager != null) {
            this.cacheManager.close();
        }
        if (this.databaseManager != null) {
            this.databaseManager.close();
        }

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件卸载成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");

        this.bungeeCordManager = null;
        this.taskManager = null;
        this.listenerManager = null;
        this.commandManager = null;
        this.bStatsManager = null;
        this.pluginHookManager = null;
        this.cacheManager = null;
        this.databaseManager = null;
        this.librariesManager = null;
        this.configManager = null;

        instance = null;
    }
}

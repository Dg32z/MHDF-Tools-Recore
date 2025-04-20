package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.manager.*;
import cn.chengzhiya.mhdftools.manager.database.MHDFDatabaseManager;
import cn.chengzhiya.mhdftools.util.message.LogUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {
    public static Main instance;

    private LogFilterManager logFilterManager;
    private LibrariesManager librariesManager;
    private MinecraftLangManager minecraftLangManager;
    private ConfigManager configManager;

    private AdventureManager adventureManager;
    private MHDFDatabaseManager databaseManager;
    private CacheManager cacheManager;
    private PluginHookManager pluginHookManager;
    private CommandManager commandManager;
    private ListenerManager listenerManager;
    private TaskManager taskManager;
    private BungeeCordManager bungeeCordManager;
    private BStatsManager bStatsManager;

    @Override
    public void onLoad() {
        instance = this;

        this.logFilterManager = new LogFilterManager();
        this.logFilterManager.init();

        this.librariesManager = new LibrariesManager();
        this.librariesManager.init();

        this.minecraftLangManager = new MinecraftLangManager();
        this.minecraftLangManager.init();

        this.configManager = new ConfigManager();
        this.configManager.init();
    }

    @Override
    public void onEnable() {
        this.adventureManager = new AdventureManager();
        this.adventureManager.init();

        this.databaseManager = new MHDFDatabaseManager();
        this.databaseManager.connect();
        this.databaseManager.initTable();

        this.cacheManager = new CacheManager();
        this.cacheManager.init();

        this.pluginHookManager = new PluginHookManager();
        this.pluginHookManager.hook();

        this.commandManager = new CommandManager();
        this.commandManager.init();

        this.listenerManager = new ListenerManager();
        this.listenerManager.init();

        this.taskManager = new TaskManager();
        this.taskManager.init();

        this.bungeeCordManager = new BungeeCordManager();
        this.bungeeCordManager.init();

        this.bStatsManager = new BStatsManager();
        this.bStatsManager.init();

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件启动成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }

    @Override
    public void onDisable() {
        if (this.bungeeCordManager != null) {
            this.bungeeCordManager.close();
        }
        if (this.pluginHookManager != null) {
            this.pluginHookManager.unhook();
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

        if (this.adventureManager != null) {
            this.adventureManager.close();
        }

        this.bStatsManager = null;
        this.bungeeCordManager = null;
        this.taskManager = null;
        this.listenerManager = null;
        this.commandManager = null;
        this.pluginHookManager = null;
        this.cacheManager = null;
        this.databaseManager = null;
        this.adventureManager = null;
        this.configManager = null;
        this.minecraftLangManager = null;
        this.librariesManager = null;
        this.logFilterManager = null;

        instance = null;
    }
}

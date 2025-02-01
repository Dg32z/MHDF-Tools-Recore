package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.manager.*;
import cn.ChengZhiYa.MHDFTools.util.message.LogUtil;
import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main instance;
    public static BukkitAudiences adventure;
    @Getter
    private ConfigManager configManager;
    @Getter
    private LibrariesManager librariesManager;
    @Getter
    private DatabaseManager databaseManager;
    @Getter
    private CacheManager cacheManager;
    @Getter
    private CommandManager commandManager;
    @Getter
    private ListenerManager listenerManager;
    @Getter
    private PluginHookManager pluginHookManager;
    @Getter
    private TaskManager taskManager;

    @Override
    public void onLoad() {
        instance = this;

        this.configManager = new ConfigManager();
        this.configManager.init();

        this.librariesManager = new LibrariesManager();
        this.librariesManager.init();
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        this.pluginHookManager = new PluginHookManager();
        this.pluginHookManager.hook();

        this.databaseManager = new DatabaseManager();
        this.databaseManager.init();

        this.cacheManager = new CacheManager();
        this.cacheManager.init();

        this.commandManager = new CommandManager();
        this.commandManager.init();

        this.listenerManager = new ListenerManager();
        this.listenerManager.init();

        this.taskManager = new TaskManager();
        this.taskManager.init();

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件启动成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
    }

    @Override
    public void onDisable() {
        this.pluginHookManager.unhook();
        this.pluginHookManager = null;

        this.databaseManager.close();
        this.databaseManager = null;

        this.cacheManager.close();
        this.cacheManager = null;

        this.configManager = null;
        this.listenerManager = null;
        this.commandManager = null;
        this.taskManager = null;

        if (adventure != null) {
            adventure.close();
            adventure = null;
        }

        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");
        LogUtil.log("&a插件启动成功! 官方交流群: 129139830");
        LogUtil.log("&e-----------&6=&e梦之工具&6=&e-----------");

        instance = null;
    }
}

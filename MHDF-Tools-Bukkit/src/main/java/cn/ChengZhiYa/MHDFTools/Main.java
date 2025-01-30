package cn.ChengZhiYa.MHDFTools;

import cn.ChengZhiYa.MHDFTools.manager.init.*;
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

        this.commandManager = new CommandManager();
        this.commandManager.init();

        this.listenerManager = new ListenerManager();
        this.listenerManager.init();

        this.taskManager = new TaskManager();
        this.taskManager.init();
    }

    @Override
    public void onDisable() {
        this.pluginHookManager.unhook();
        this.databaseManager.close();

        instance = null;
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }
}

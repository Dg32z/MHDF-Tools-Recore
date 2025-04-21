package cn.chengzhiya.mhdftools;

import cn.chengzhiya.mhdftools.listener.PluginMessage;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Getter
@Plugin(
        id = "mhdf-tools-velocity",
        name = "MHDF-Tools-Velocity",
        version = "3.0.0"
)
public final class Main {
    public static Main instance;

    private final ProxyServer server;
    private final Logger logger;
    private final File dataFolder;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDirectory.toFile();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        getServer().getChannelRegistrar().register(PluginMessage.IDENTIFIER);
    }
}

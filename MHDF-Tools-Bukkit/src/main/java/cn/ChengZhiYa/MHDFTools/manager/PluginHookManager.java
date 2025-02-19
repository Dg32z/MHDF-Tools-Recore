package cn.ChengZhiYa.MHDFTools.manager;

import cn.ChengZhiYa.MHDFTools.hook.*;
import cn.ChengZhiYa.MHDFTools.interfaces.Hook;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class PluginHookManager implements Hook {
    private final PacketEventsHook packetEventsHook = new PacketEventsHook();
    private final PlaceholderAPIHook placeholderAPIHook = new PlaceholderAPIHook();
    private final CraftEngineHook craftEngineHook = new CraftEngineHook();
    private final MythicMobsHook mythicMobsHook = new MythicMobsHook();
    private final VaultHook vaultHook = new VaultHook();

    /**
     * 初始化所有对接的API
     */
    @Override
    public void hook() {
        packetEventsHook.hook();
        placeholderAPIHook.hook();
        craftEngineHook.hook();
        mythicMobsHook.hook();
        vaultHook.hook();
    }

    /**
     * 卸载所有对接的API
     */
    @Override
    public void unhook() {
        packetEventsHook.unhook();
        placeholderAPIHook.unhook();
        craftEngineHook.unhook();
        mythicMobsHook.unhook();
        vaultHook.unhook();
    }
}

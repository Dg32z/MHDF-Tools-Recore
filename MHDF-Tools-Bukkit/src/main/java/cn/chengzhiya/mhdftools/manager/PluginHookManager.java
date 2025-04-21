package cn.chengzhiya.mhdftools.manager;

import cn.chengzhiya.mhdftools.hook.*;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public final class PluginHookManager {
    private final PacketEventsHook packetEventsHook = new PacketEventsHook();
    private final PlaceholderApiHook placeholderAPIHook = new PlaceholderApiHook();
    private final CraftEngineHook craftEngineHook = new CraftEngineHook();
    private final MythicMobsHook mythicMobsHook = new MythicMobsHook();
    private final VaultHook vaultHook = new VaultHook();

    /**
     * 初始化所有对接的API
     */
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
    public void unhook() {
        packetEventsHook.unhook();

        placeholderAPIHook.unhook();
        craftEngineHook.unhook();
        mythicMobsHook.unhook();
        vaultHook.unhook();
    }
}

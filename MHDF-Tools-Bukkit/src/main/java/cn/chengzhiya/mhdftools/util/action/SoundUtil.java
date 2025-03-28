package cn.chengzhiya.mhdftools.util.action;

import cn.chengzhiya.mhdftools.Main;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class SoundUtil {
    /**
     * 获取指定音效key的音效实例
     *
     * @param key 音效key
     * @return 音效实例
     */
    public static Sound getSound(String key) {
        if (Main.instance.getPluginHookManager().getPacketEventsHook().getServerVersion()
                .isNewerThanOrEquals(ServerVersion.V_1_21)
        ) {
            return Registry.SOUNDS.get(NamespacedKey.minecraft(key
                    .replace("_", ".")
                    .toLowerCase(Locale.ROOT))
            );
        }

        try {
            Method method = Sound.class.getDeclaredMethod("valueOf", String.class);
            method.setAccessible(true);
            Object object = method.invoke(Sound.class, key.toUpperCase(Locale.ROOT));
            return (Sound) object;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

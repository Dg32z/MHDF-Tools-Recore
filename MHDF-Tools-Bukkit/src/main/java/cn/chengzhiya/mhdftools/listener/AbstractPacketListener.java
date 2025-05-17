package cn.chengzhiya.mhdftools.listener;

import cn.chengzhiya.mhdftools.util.config.ConfigUtil;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public abstract class AbstractPacketListener implements PacketListener {
    private final boolean enable;
    private final PacketListenerPriority priority;

    public AbstractPacketListener(@NotNull PacketListenerPriority priority) {
        this.enable = true;
        this.priority = priority;
    }

    public AbstractPacketListener(@NotNull String enableKey, @NotNull PacketListenerPriority priority) {
        this.enable = ConfigUtil.getConfig().getBoolean(enableKey);
        this.priority = priority;
    }

    public AbstractPacketListener(List<String> enableKeyList, @NotNull PacketListenerPriority priority) {
        boolean enable = true;
        for (String enableKey : enableKeyList) {
            if (enableKey == null || enableKey.isEmpty()) {
                continue;
            }

            enable = ConfigUtil.getConfig().getBoolean(enableKey);
        }

        this.enable = enable;
        this.priority = priority;
    }
}
